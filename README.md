# FakeRESTApi Online Bookstore
https://fakerestapi.azurewebsites.net/index.html
## Automation project

1. Test runner: JUnit
2. Report tool: Allure
3. Framework for API testing: RestAssured
4. CI/CD: GitHub Actions

The test framework is designed as a set of independent modules. The `api-framework` module encapsulates the logic for 
interacting with the API. The `api-tests` module contains the test scenarios and assertions. The `data-generator` module 
is responsible for creating test data. The `utils` module provides various utility methods and constants that can be 
reused across all modules.

This modular architecture promotes clear separation of concerns and ensures that the logic of each module remains 
isolated from the others, so changes in one module do not affect the others. For example, the `api-tests` module 
has no knowledge of the specific library used in`api-framework` for working with API requests. We can replace 
`RestAssured` with `HTTP Client` without making any changes to the `api-tests` module. Similarly, the `api-framework` 
module does not depend on the test runner framework, so we can switch from `JUnit` to `TestNG` without impacting 
the `api-framework`.

To make the framework more flexible, it’s recommended to add a `launcher` module for running tests in different 
environments, with various parameters, tags, etc. It is also worth adding a `reporter` module to integrate the framework 
with a test management system, if one is used on the project.

### Usage guide
Please, make sure that Java 17 is used to build and run tests on your machine.

For maven command one can use both system `mvn` binary or the provided maven wrapper(preferred), `mvnw`.
For the latter in Unix system it might require permission to be executed, the permission is provided by issuing in 
terminal: `chmod +x mvnw`
1. Build the project and run tests:   
   `mvn clean install`
2. Generate Allure report:   
   `mvn allure:serve -pl api-tests`
3. The parallel execution strategy can be changed in the `junit-platform.properties` file.

#### Allure report cleanup
Allure report is generated taking into account existing execution results, so it should be cleared before running new set
of tests if it is needed to have 'clean'/'fresh' report. It could be done by rebuilding (`mvn clean install`) but it is
quite time-consuming way.

#### Running the Workflow
1. Go to the Actions tab of the repository:
   https://github.com/valeriisukhyna/bookstore-fakerestapi-tests/actions
2. Select the workflow: `Run Automation Tests`
3. Click the Run workflow button, select the desired branch (by default thi is `main`), and confirm.
#### Viewing the Report
After the workflow finishes, the Allure report is automatically deployed via GitHub Pages.
You can view the latest test results at:

`Run Automation Tests -> Tests summary -> Allure Report -> Click here to view Allure Report`

Note: the report always shows the results of the most recent run.

#### Workflow configuration
The workflow is defined in `.github/workflows/automation-tests.yml`.

Deployment uses `peaceiris/actions-gh-pages`.

The report is published to the `gh-pages` branch and served via GitHub Pages.