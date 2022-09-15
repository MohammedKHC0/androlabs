package com.rivan.androidplaygrounds.android.compiler.exec

import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.android.tools.r8.D8
import com.android.tools.r8.D8Command
import com.android.tools.r8.OutputMode
import com.rivan.androidplaygorunds.project.utils.CoroutineUtil
import com.rivan.androidplaygorunds.project.utils.FileUtil
import com.rivan.androidplaygorunds.project.utils.MultipleDexClassLoader
import com.rivan.androidplaygorunds.project.utils.interfaces.Project
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.nio.file.Paths

class ExecuteDexTask(
    val preferences: SharedPreferences,
    val clazz: String,
    val inputStream: InputStream,
    val outputStream: PrintStream,
    val errorStream: PrintStream,
    val postRunnable: Runnable
) {

    private var result: Any? = null

    fun getTaskName(): String = "Execute Dex Task"

    /*
     * Runs the main method of the program by loading it through
     * PathClassLoader
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun doTask(project: Project) {
        val defaultIn = System.`in`
        val defaultOut = System.`out`
        val defaultError = System.err
        val dexFile = project.getBinDirPath() + "classes.dex"
        System.setOut(outputStream)
        System.setErr(errorStream)
        System.setIn(inputStream)

        // Load the dex file into a ClassLoader
        val dexLoader = MultipleDexClassLoader()

        dexLoader.loadDex(dexFile)

        val libs = File(project.getLibDirPath()).listFiles()
        if (libs != null) {
            // check if all libs have been pre-dexed or not
            for (lib in libs) {
                val outDex = project.getBuildDirPath() + lib.name.replaceAfterLast('.', "dex")

                if (!File(outDex).exists()) {
                    CoroutineUtil.inParallel {
                        D8.run(
                            D8Command.builder()
                                .setOutput(
                                    Paths.get(project.getBuildDirPath()),
                                    OutputMode.DexIndexed
                                )
                                .addLibraryFiles(
                                    Paths.get(
                                        FileUtil.getClasspathDir(),
                                        "android.jar"
                                    )
                                )
                                .addProgramFiles(lib.toPath())
                                .build()
                        )
                        File(project.getBuildDirPath(), "classes.dex").renameTo(File(outDex))
                    }
                }
                // load library into ClassLoader
                dexLoader.loadDex(outDex)
            }
        }

        val loader = dexLoader.loadDex(FileUtil.getClasspathDir() + "kotlin-stdlib-1.7.20-Beta.jar")

        val args = preferences.getString("program_arguments", "")!!.trim()

        // Split arguments into an array
        val param = args.split("\\s+").toTypedArray()

        CoroutineUtil.inParallel {
            try {
                val calledClass = loader.loadClass(clazz)

                val method = calledClass.getDeclaredMethod("main", Array<String>::class.java)
                if (Modifier.isStatic(method.getModifiers())) {
                    // If the method is static, directly call it
                    result = method.invoke(null, param as? Any)
                } else if (Modifier.isPublic(method.getModifiers())) {
                    // If the method is public, create an instance of the class,
                    // and then call it on the instance
                    val classInstance = calledClass.getConstructor().newInstance()
                    result = method.invoke(classInstance, param as? Any)
                }
                // print the value of the method if it's not null
                if (result != null) {
                    println(result.toString())
                }
            } catch (e: InvocationTargetException) {
                e.getTargetException().printStackTrace(errorStream)
            } catch (e: Throwable) {
                e.printStackTrace(errorStream)
            } catch (e: Error) {
                e.printStackTrace(errorStream)
            }
            System.setOut(defaultOut)
            System.setErr(defaultError)
            System.setIn(defaultIn)
            Handler(Looper.getMainLooper()).post(postRunnable);
        }
    }
}
