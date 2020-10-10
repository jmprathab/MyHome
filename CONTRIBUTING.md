# Contributing

Firstly, thanks for taking time to contribute to this reporitory! People like you power this open source community.

Please note we have a [code of conduct](CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

## How to file a bug report or feature request?

1. This repository has issue, feature request and pull request templates which would autofill when you raise a bug, request for a new feature or when you raise a PR. Do your best to fill out the template completely.

## Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a
   build.
2. Update the README.md with details of changes to the interface, this includes new environment
   variables, exposed ports, useful file locations and container parameters.
3. Increase the version numbers in any examples files and the README.md to the new version that this
   Pull Request would represent. The versioning scheme we use is [SemVer](http://semver.org/).
4. Pull requests will be approved by one of the owners of this repository.
5. Pattern for git branch name has to be `feature/<ISSUE_NUMBER>--<ISSUE_SHORT_NAME>`
6. Pattern for pull request title has to be `Issue <ISSUE_NUMBER>: <ISSUE_NAME>`
7. One PR for one feature/bug. Don't target multiple Features or Bugs in a single PR unless they are related and cannot be breaken down further.

## How to set up your environment and run tests?

1. Building and installation instructions are present in README.md file.

## Coding Style
Coding style helps to have consistent code. 
This project uses coding style from https://github.com/square/java-code-styles


### Set Code Style XML
The easiest way is download the xml file from: https://github.com/square/java-code-styles/blob/main/configs/codestyles/Square.xml.
Then you can import it in Intellij.
* File → Settings → Editor → Code Style
* Select the small gear icon next to "Scheme", select "Import Scheme" → "IntelliJ IDEA code style XML"
* Select the Square.xml
* Select OK, then Apply, then OK
* Ensure the "Square" Code Style Scheme is selected.

Note: Above instructions are based on IntelliJ 2019.2.3 version.
If above steps are not working/changed, refer Intellij documentation corresponding to your version.
