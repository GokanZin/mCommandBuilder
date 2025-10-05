# 🌟 mCommandBuilder | Crie Comandos de Forma Simples e Rápida!

MCommandBuilder é uma API simples e flexível para criar comandos no Bukkit/Spigot com execução personalizável, verificação de permissão e auto-completar!

Funcionando atualmente para versões 1.5.2 até 1.21

---



## 📌 Como Usar

### 🔹 Criando um Comando Simples

```java
new CommandBuilder("meucomando")
    .setAliases("mc", "cmd")
    .setPermission("meucomando.use")
    .setMessagePermission("§cVocê não tem permissão para executar esse comando!")
    .executor((sender, args) -> {
        sender.sendMessage("§aComando executado com sucesso!");
    })
    .setTabCompleter((sender, args) -> Arrays.asList("opcao1", "opcao2"))
    .registerCommand(plugin);
```

---

### 🔹 Exemplo Avançado: Melhor Organização

Criamos uma classe separada para organizar melhor os comandos:

#### 📌 Criando um Comando `/gm`

```java
public class CommandGamemode {

    public void register(JavaPlugin plugin){
        new CommandBuilder("gm")
                .setAliases("gamemode", "gms", "gamemodes") // Adiciona aliases para o comando
                .setPermission("gamemode.use")
                .setMessagePermission("§cVocê não tem permissão para executar esse comando!")
                .executor((sender, args) -> { // Executa o comando
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cVocê precisa ser um jogador para executar esse comando!");
                        return;
                    }

                    Player player = (Player) sender;
                    if (args.length == 0) {
                        sender.sendMessage("§cUso: /gm <gamemode>");
                        return;
                    }
                    String mode = args[0];
                    switch (mode) {
                        case "0":
                            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                            sender.sendMessage("§aModo de jogo alterado para Survival!");
                            break;
                        case "1":
                            player.setGameMode(org.bukkit.GameMode.CREATIVE);
                            sender.sendMessage("§aModo de jogo alterado para Criativo!");
                            break;
                        default:
                            sender.sendMessage("§cModo de jogo inválido! Use /gm 0 ou /gm 1.");
                            break;
                    }
                })
                .setTabCompleter((sender, args) -> { // Auto completar o comando
                    if (args.length == 1) {
                        return Arrays.asList("0", "1");
                    }
                    return Collections.emptyList();
                })
                .registerCommand(plugin);
    }
}
```

#### 📌 Registrando na Classe Principal `Main.java`

```java
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        new CommandGamemode().register(this);
    }
}
```

---

# MCommandBuilder

O **MCommandBuilder** é uma biblioteca que facilita a criação e gerenciamento de comandos em projetos Java, especialmente para plugins em ambientes como Spigot/Bukkit. Com ele, você pode construir comandos de forma mais organizada e eficiente.

## 📦 Instalação

### 🔹 Usando JitPack

Para usar o **MCommandBuilder** em seu projeto, você pode instalá-lo facilmente através do JitPack. Siga os passos abaixo:

#### Para Gradle

1. Adicione o plugin Shadow ao seu arquivo `build.gradle`:

    ```groovy
    plugins {
        id 'com.github.johnrengelman.shadow' version '8.3.0'
    }
    ```

2. Adicione o repositório JitPack ao seu arquivo `build.gradle`:

    ```groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }
    ```

3. Adicione a dependência do **MCommandBuilder**:

    ```groovy
    dependencies {
        implementation 'com.github.GokanZin:mCommandBuilder:1.1'
    }
    ```

4. Configure o `shadowJar` para relocar pacotes (se necessário):

    ```groovy
    shadowJar {
        // Substitua 'com.yourpackage' pelo pacote do seu plugin 
        relocate 'br.com.gokan.mcommandbuilderv2', 'com.yourpackage.mcommandbuilder'
    }
    ```

#### Para Maven

1. Adicione o repositório JitPack ao seu arquivo `pom.xml`:

    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

2. Adicione a dependência do **MCommandBuilder**:

    ```xml
    <dependency>
        <groupId>com.github.GokanZin</groupId>
        <artifactId>mCommandBuilder</artifactId>
        <version>1.1</version>
    </dependency>
    ```

#### 🔹 Exemplo Completo do `pom.xml`

Aqui está um exemplo completo de como seu arquivo `pom.xml` pode ficar:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.seu.pacote</groupId>
    <artifactId>seu-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.GokanZin</groupId>
            <artifactId>mCommandBuilder</artifactId>
            <version>1.1</version>
        </dependency>
    </dependencies>
</project>
