lateinit var testReportOutputPath: String
lateinit var integrationTestReportOutputPath: String
val integrationTestTask = tasks.register<Test>("integrationTest") {
    group = "verification"
    useJUnitPlatform {
        includeTags("integration")
    }

    integrationTestReportOutputPath = reports.html.outputLocation.toString()
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("integration")
    }

    testReportOutputPath = reports.html.outputLocation.toString()
}

tasks.named("check").configure {
    dependsOn(integrationTestTask)
}

val deleteIndexHtmlReportTask: Delete = tasks.register<Delete>("deleteIndexHtmlReportTask").get()

tasks.register<Copy>("renameTestReportTask") {
    from(testReportOutputPath)
    into(integrationTestReportOutputPath)
    include("index.html")
    rename("index.html", "unit-test-report.html")

    finalizedBy(deleteIndexHtmlReportTask)
    doLast {
        val testReportOutputFile = "$testReportOutputPath/index.html"
        deleteIndexHtmlReportTask.delete(testReportOutputFile)
    }
}

tasks.register<Copy>("renameIntegrationTestSummaryReportTask") {
    include("index.html")
    rename("index.html", "integration-test-report.html")
    finalizedBy(deleteIndexHtmlReportTask)
    doLast {
        val testReportOutputFile = "$integrationTestReportOutputPath/index.html"
        deleteIndexHtmlReportTask.delete(testReportOutputFile)
    }
}