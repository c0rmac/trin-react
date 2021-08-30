package com.trinitcore.trinreact.precompilation.reflection

import com.fasterxml.jackson.databind.ObjectMapper
import com.trinitcore.trinreact.precompilation.GeneratorConfig
import java.io.File

interface Serializer {
    val buildDirs: Array<String>
    fun persist(tReflectionContext: TReflectionContext)
}

class JSONSerializer(
    /** The target paths for where the main.json are to be generated. */
    override val buildDirs: Array<String>,
    private val generatorConfig: GeneratorConfig
) : Serializer {

    override fun persist(tReflectionContext: TReflectionContext) {
        for (path in generatorConfig.buildDirsJson) {
            val dir = File(path)
            if (!dir.exists()) dir.mkdirs()
            ObjectMapper().writeValue(File("$path/main.json"), tReflectionContext)
        }
    }

}

class CodeGeneratingSerializer(
    /** The target paths for which hold directory paths com.precompilation. A _Generated.kt file will be created in the
     * com/clickcostz/precompilation directory */
    override val buildDirs: Array<String>
) : Serializer {

    companion object {
        const val DEFAULT_PACKAGE = "com.clickcostz.precompilation"
        const val DEFAULT_SUB_DIR = "com/clickcostz/precompilation"
        const val DEFAULT_FILE_NAME = "_Generated.kt"
    }

    override fun persist(tReflectionContext: TReflectionContext) {
        var body = ""
        // TUS : Generate imports
        fun appendImport(qualifiedPackage: String, name: String) {
            body += "import ${qualifiedPackage}.${name}\n"
        }

        // tReflectionContext.classes.forEach { appendImport(it.qualifiedPackage, it.name) }
        // tReflectionContext.functions.forEach { appendImport(it.qualifiedPackage, it.name) }
        appendImport("com.clickcostz.common.precompilation.reflection", "TClass")
        appendImport("com.clickcostz.common.precompilation.reflection", "TFunction")
        appendImport("com.clickcostz.common.precompilation.reflection", "TClassType")
        appendImport("com.clickcostz.common.precompilation.reflection", "TParam")
        appendImport("com.clickcostz.common.precompilation.reflection", "TReturnType")
        // DEIREADH : Generate imports

        body += "\n"

        // TUS : Define serializers
        fun <T>getSerializedArray(iterable: Array<T>, itemBlock: (item: T) -> String): String {
            var serialized = "arrayOf(\n"
            iterable.forEach { serialized += "${itemBlock(it)},\n" }
            serialized += ")"

            return serialized
        }

        fun getSerializedTReturnType(tReturnType: TReturnType): String {
            var serialized = "TReturnType(\n"
            serialized += "\"${tReturnType.qualifiedName}\",\n"
            serialized += getSerializedArray(tReturnType.typeParameters) {
                "\"$it\""
            }
            serialized += ")"

            return serialized
        }

        fun getSerializedTParam(tParam: TParam): String {
            var serialized = "TParam(\n"
            serialized += "\"${tParam.qualifiedPackage}\",\n"
            serialized += "\"${tParam.name}\",\n"
            serialized += "\"${tParam.type}\",\n"
            serialized += ")"
            
            return serialized
        }

        fun getSerializedTFunction(tFunction: TFunction): String {
            var serialized = "TFunction(\n"
            serialized += "\"${tFunction.qualifiedPackage}\",\n"
            serialized += "\"${tFunction.name}\",\n"
            serialized += getSerializedArray(tFunction.params) {
                getSerializedTParam(it)
            }
            serialized += ",\n"
            serialized += getSerializedTReturnType(tFunction.returnType)
            serialized += ")"

            return serialized
        }

        fun getSerializedTClass(tClass: TClass): String {
            var serialized = "TClass(\n"
            serialized += "\"${tClass.qualifiedPackage}\",\n"
            serialized += "\"${tClass.name}\",\n"
            serialized += getSerializedArray(tClass.functions) {
                getSerializedTFunction(it)
            }
            serialized += ",\n"
            serialized += getSerializedArray(tClass.classes) {
                getSerializedTClass(it)
            }
            serialized += ",\n"
            serialized += "TClassType."+tClass.type.name + "\n"
            serialized += ")"

            return serialized
        }
        // DEIREADH : Define serializers

        // TUS : Declare tClasses
        tReflectionContext.classes.forEachIndexed { index, tClass -> body += "val tClass${index} = ${getSerializedTClass(tClass)}\n" }
        body += "\n\n"
        // DEIREADH : Declare tClasses

        // TUS : Generate mapping
        body += "internal val kClassesToTClasses = mapOf(\n"
        tReflectionContext.classes.forEachIndexed { index, tClass -> body += "${tClass.qualifiedPackage}.${tClass.name}::class to tClass${index},\n" }
        body += ")\n\n"

        body += "internal val qualifiedPackagesToKClasses = mapOf(\n"
        tReflectionContext.classes.forEachIndexed { index, tClass -> body += "\"${tClass.qualifiedPackage}.${tClass.name}\" to ${tClass.qualifiedPackage}.${tClass.name}::class,\n" }
        body += ")"
        // DEIREADH : Generate mapping

        for (buildDir in buildDirs) {
            val document = "package $DEFAULT_PACKAGE\n\n${body}"
            val filePath = "$buildDir/$DEFAULT_SUB_DIR/$DEFAULT_FILE_NAME"
            val file = File(filePath)
            file.delete()
            file.createNewFile()
            file.writeText(document)
        }
    }

}