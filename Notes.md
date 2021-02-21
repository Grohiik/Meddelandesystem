# Meddelandesystem Notes

## Requirements

### Server
- [ ] Multiple clients.
- [ ] Client have the ability to disconnect.
- [ ] Update the connected clients with list of connected clients.
- [ ] User sends one message to multiple recipients?
- [ ] Save the unsent messages. (The user message trying to reach is offline).
- [ ] Send the unsent messages. (The user message trying to reach is online).
- [ ] Log the traffic to a file.

### Client
- [ ] Connect to the server.
- [ ] Disconnect from the server.
- [ ] Sends a message.
- [ ] Receive a message.
- [ ] Show connected users.
- [ ] Choose username and profile image before connecting to the server.
- [ ] Have the ability to add the connected user on the server to the personal contact list.
- [ ] The contact list are to be stored on the disk.
- [ ] When relaunching it should be reading the file stored on the disk to load th settings, (Contact list, Username, Profile image).
- [ ] The process of writing text and choosing image should be easy for the user. (JFileChooser). JPEG or PNG works and the text can be plain text.

## Design of the system

### Shared

#### Entity

```java
class User {}
```

```java
class Message {}
```

```java
class Clients {}
```

### Server

#### Control

```java
class ServerController {}
```

#### Entity

```java
class UnsentMessages {}
```

### Client

#### Boundary

```java
class ClientUI {}
```

#### Control

```java
class ClientController {}
```