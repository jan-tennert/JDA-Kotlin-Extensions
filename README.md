# JDA-Ktx

JDA-Ktx is a package for Discord.JDA which uses Kotlin-Only features like Type-Safe Builders and it has a built in SlashCommandHandler.

# Example:

```kotlin
val jda = JDABuilder.createDefault("token").build()
val commandHandler = CommandHandler(jda)

jda.awaitReady()
val test = testCommand()
//If it a guild only command you can do
test.guildID = 98318748314
commandHandler.registerCommands(test)

class testCommand : Command("test", "This is a Test Command") {
      override fun run(
            channel: TextChannel?,
            member: Member?,
            user: User,
            privateChannel: PrivateChannel?,
            hook: CommandHook,
            options: MutableList<SlashCommandEvent.OptionData>,
            event: SlashCommandEvent
        ) {
            val embed = messageEmbed {
                title("Title")
                field() {
                    name = "Field"
                    value = "Value"
                    inline = true
                }
                timestamp(Date())
                footer("Footer")
            }
            event.reply(embed).queue()
        }

}
```
