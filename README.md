# Meddelandesystem

This program is intended to work similar to how MSN did and is developed as a group project for a university course.

## Development

Please read through this before writing anything to the codebase.

**Using IntelliJ**

To get started on you'll need either maven installed or IntelliJ.

For IntelliJ you'll need to open the `pom.xml` file in this project and it'll prompt you if you want to open it as project or not. Choose the one that's

### Code Style

- The **package** name should be all lowercase. 

- When formatting your code you'll need to use `clang-format` with the included `.clang-format`.

- The codebase use 4 spaces as indentation.

**Getting clang-format**

For **Windows** the clang-format can be found at https://llvm.org/builds. For the **macOS** you can use `brew` to install and Linux it is best to use your package manager.

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