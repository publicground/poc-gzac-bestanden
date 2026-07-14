val environmentVariablesFileName = ".env.properties"

// loads environment variables when a bootRun task is ran

tasks.withType<JavaExec> {
    if (this.name.startsWith("bootRun")) {
        println("   - Configuring environment variables for task [${this.name}]")

        project.file(environmentVariablesFileName)
            .takeIf { it.exists() && it.isFile }
            ?.run {
                readLines()
                    .filterNot { it.startsWith("#") || it.startsWith("//") || it.isEmpty() }
                    .map { it.split("=", limit = 2) }
                    .forEach { environment(it.first(), it.last()) }
            }
            ?: println("   - Skipping environment variable initialisation: No '$environmentVariablesFileName' file found.")
    }
}