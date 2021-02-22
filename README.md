# Meddelandesystem

This program is intended to work similar to how MSN did and is developed as a group project for a university course.

To get started with development please head over to [Development](#development) section to setup your environments.

## Development

Please read through this before writing anything to the codebase. The project uses maven as build system and it is IDE agnostic. This means that you can use your favorite IDE or Editor.

For formatting code and to run the code you'll need to follow the guide below.

**Using IntelliJ**

For IntelliJ you'll need to open the `pom.xml` file in this project and it'll prompt you if you want to open it as project or not. Choose `Open as Project`.

### Code Style

- The **package** name should be all lowercase.

- When formatting your code you'll need to use `clang-format` with the included `.clang-format`.

- The codebase use 4 spaces as indentation.

- camelCase

- Constant value should be in SNAKE_CASE with all CAPS.

- Use clang-format to fix the code format

#### Getting clang-format

For **Windows** the clang-format can be found at https://releases.llvm.org/, head over to the latest `download` link, scroll down to **Pre-Built Binaries** and download the Windows one.

**NOTE (Windows Install)**: When installing make sure you select `Add LLVM to the system PATH for ...`. This makes sure that you can type `clang-format` in your terminal.

For the **macOS** you can use `brew` to install and **Linux** it is best to use your package manager.

**NOTE**: When formatting make sure you don't put extra newline before opening brace. `clang-format` will always keep at least one newline. Example down below is only the illustration on what not to do when formatting with `clang-format`. This is not how the style should look like.

```cpp
void hello()

{} // BAD
```

```cpp
void hello()
{} // OK
```

#### Terminal

You can format your code in the terminal with this command

```
clang-format -i <filepath>
```

#### IntelliJ

To use `clang-format` with **IntelliJ**, there are two ways. One is to use this extension [ClangFormat](https://plugins.jetbrains.com/plugin/13359-clangformat). This will add the option in `Code → Reformat Code with clang-format`, you'll be using this option instead of the regular `Reformat Code`.

The second one is the set up External Tools. This guide can be followed down below in [IntelliJ External Tools](#external-tools).

**NOTE (Windows)**: If the extension doesn't format your code you can go to `File → Settings... → Tools → clang-format`. In the `clang-format binary` you can try adding `clang-format.exe` instead. If it doesn't work you can specify the `PATH` that points to where `LLVM/bin` is in your system. If this doesn't fix your problem then you can follow the guide below on how to set up External Tools.

**NOTE IntelliJ**: If the extension doesn't work you can [set up an External Tool](#external-tools) that runs the clang-format for formatting the code.

##### External Tools

To create a tool you can

1. Go to `File → Settings... → Tools → External Tools`.
2. Add a new tool by pressing the plus icon.
3. Give it a name such as `clang-format`.
4. In `Program:` field you can take the whole path to where clang-format executable is on your system. If your environment variable is already set you can just input `clang-format`.
5. In `Arguments:` you need to input these values `-i $FilePath$`.
6. In `Working directory:` you need `$FileDir$`

Now when you want to format your code you can go to `Tools → External Tools → clang-format` and it'll run the `clang-format` command for you and reformat the file you're currently looking at.

### Project Structure

```
Meddelandesystem
├─ src
│  ├─ main
│  │  └─ java
│  │     ├─ client
│  │     │  ├─ boundary
│  │     │  ├─ control
│  │     │  ├─ entity
│  │     │  └─ StartClient.java
│  │     ├─ server
│  │     │  ├─ control
│  │     │  ├─ entity
│  │     │  └─ StartServer.java
│  │     └─ shared
│  │        └─ entity
│  │           └─ User.java
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

This will generate the the `class` files in `target/classes` directory. You can use these class files to run the main method.

**Start the server**

```shell
java -cp target/classes server.StartServer
```

**Start the client**

```shell
java -cp target/classes client.StartClient
```

### Generate JavaDoc

The javadoc can be generate in the following way. The output directory should be in `docs/apidocs`. The apidocs is ignored in the repo.

**Using maven**

Use this command to generate the javadoc

```
mvn javadoc:javadoc
```

This will created an html page in the `docs/apidocs`.

**Using Intellij**

Go to → Tools → Generate JavaDoc...

A Window will pop up, select `Whole project` and set `Output directory` to `docs/apidocs` and press `OK`. Javadoc will generate html docs for **you**.

### Production Builds

This section covers how to generated the `server` and `client` jar for distribution and usage.

**Using Maven**

On maven you can run this command to generate the `jar` files.

```shell
mvn package
```

Two jar files will be generated, one for the client and one for the server.

**Using IntelliJ**

Open this project in IntelliJ, there should be a tab on the right side that has an icon with letter "M" and name under that says "Maven". Open it up and you'll see the project name, expand it and you'll see `Lifecycle` and `Plugins`. Expand the `Lifecycle` and use the command **package**. This will generate the jar files in the target directory.
