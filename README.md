# 🌟 mCommandBuilder - Construa Comandos de Forma Simples e Rápida!

MCommandBuilder é uma API simples e flexível para criar comandos no Bukkit/Spigot com execução personalizável, verificação de permissão e auto-completar!
Funcionando atualmente para versões 1.5.2 até 1.21
---

## 📌 Como Usar

### 🔹 Criando um Comando Simples

```java
new CommandBuilder("meucomando")
    .setAliases("mc", "cmd")
    .setPermission("meucomando.use")
    .setMessagePermission("\u00a7cVocê não tem permissão para executar esse comando!")
    .executor((sender, args) -> {
        sender.sendMessage("\u00a7aComando executado com sucesso!");
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
                .setMessagePermission("\u00a7cVocê não tem permissão para executar esse comando!")
                .executor((sender, args) -> { // Executa o comando
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("\u00a7cVocê precisa ser um jogador para executar esse comando!");
                        return;
                    }

                    Player player = (Player) sender;
                    if (args.length == 0) {
                        sender.sendMessage("\u00a7cUso: /gm <gamemode>");
                        return;
                    }
                    String mode = args[0];
                    switch (mode) {
                        case "0":
                            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                            sender.sendMessage("\u00a7aModo de jogo alterado para Survival!");
                            break;
                        case "1":
                            player.setGameMode(org.bukkit.GameMode.CREATIVE);
                            sender.sendMessage("\u00a7aModo de jogo alterado para Criativo!");
                            break;
                        default:
                            sender.sendMessage("\u00a7cModo de jogo inválido! Use /gm 0 ou /gm 1.");
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

## 🚀 Benefícios do MCommandBuilder
✅ **Criação de comandos facilitada**
✅ **Execução e permissão personalizáveis**
✅ **Auto-completar eficiente**
✅ **Código mais organizado**

📌 **Use MCommandBuilder e otimize sua criação de comandos!** 🚀

