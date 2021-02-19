# Meddelandesystem

This program is intended to work similar to how MSN did and is developed as a group project for a university course.

## Development

Please read through this before writing anything to the codebase. The project uses maven as build system and it is IDE agnostic. For formatting code and to run the code you'll need to follow the guide below.

To get started you'll need either maven installed or IntelliJ.

**Using IntelliJ**

For IntelliJ you'll need to open the `pom.xml` file in this project and it'll prompt you if you want to open it as project or not. Choose `Open as Project`.

### Code Style

- The **package** name should be all lowercase. 

- When formatting your code you'll need to use `clang-format` with the included `.clang-format`.

- The codebase use 4 spaces as indentation.

**Getting clang-format**

For **Windows** the clang-format can be found at https://releases.llvm.org/, head over to the latest `download` link, scroll down to **Pre-Built Binaries** if you want to use the installer and download the one Windows one.

For the **macOS** you can use `brew` to install and **Linux** it is best to use your package manager.

To use `clang-format` with **IntelliJ** you can use this extension [ClangFormat](https://plugins.jetbrains.com/plugin/13359-clangformat). This will add the option in `Code → Reformat Code with clang-format`, you'll be using this option instead of the regular `Reformat Code`.

### Code Structure

```
Meddelandesystem
├─ src
│  ├─ main
│  │  └─ java
│  │     ├─ shared
│  │     │  └─ User.java
│  │     ├─ client
│  │     └─ server
│  └─ test
│     └─ java
├─ .clang-format
├─ .gitignore
├─ LICENSE
├─ pom.xml
└─ README.md
```

### How to run

**Using IntelliJ**

On IntelliJ you can go one of the mains and press start.

**Using Maven**

You can compile the code with this command

```shell
mvn compile
```

This will generate the the `class` files in `target/classes` directory.

**Start the server**

```shell
java -cp target/classes server.StartServer
```

**Start the client**

```shell
java -cp target/classes client.StartClient
```

### Generate JavaDoc

The javadoc can be generated in the following way. The output directory should be in `docs/apidocs`. The apidocs is ignored in the repo.

**Using maven**

Use this command to generate the javadoc

```
mvn javadoc:javadoc
```

This will created an html page in the `docs/apidocs`.

**Using Intellij**

Go to → Tools → Generate JavaDoc...

A Window will pop up, select `Whole project` and set `Output directory` to `docs/apidocs` and press `OK`. Javadoc will generate html docs for you.