# JDA-Kotlin-Extensions

JDA-Ktx is a package for Discord.JDA which uses Kotlin-Only features like Type-Safe Builders (for creating roles, text
channels, embeds) and it has a built in SlashCommandHandler, Music Manager + Event Manager (with coroutines). This
package is mainly for me but you can also contribute.

+ The Slash Commands will likely change because jda's slash commands are not done

# Features

- Event Manager with Coroutines
- CommandHandler for SlashCommands
- Type Safe Builder for slash commands, actions rows + buttons, roles, guild channels
- Music Handler

# ToDo

- Await messages etc.

# Installation

Currently you can only install this dependency through jitpack: https://jitpack.io/#jan-tennert/JDA-Ktx/

# Example:

### Slash Commands

```kotlin
val jda = JDABuilder.createDefault("token").build()
val commandHandler = CommandHandler(jda)

jda.awaitReady()

//You can create a command through a class:

commandHandler.registerCommands(testCommand())

class testCommand : Command("hi", "Say hi to the bot") {
    override fun run(
        channel: TextChannel?,
        member: Member?,
        user: User,
        privateChannel: PrivateChannel?,
        hook: InteractionHook,
        options: MutableList<OptionMapping>,
        event: SlashCommandEvent
    ) {
        event.reply("Hi, ${event.user.name}!").queue()
    }
```

Or through a type safe builder:

```kotlin
commandHandler.registerCommands(createSlashCommand {
    name = "hi"
    description = "Say hi to the bot"
    guildID = 631131922424135716
    action {
        val embed = messageEmbed {
            title = "Title!!"
            footer = "footer"
            field {
                name = "field"
                value = "value"
                inline = false
            }
        }
        it.reply(embed).queue()
    }
})
```

### Buttons

You can add buttons easy with our type safe builder:

```kotlin
val message = MessageBuilder()
    .setActionRows(actionRowBuilder(jda) { //If you pass your jda instance in the builder, you can listen to button clicks directly here in the builder as shown below 
        row { //You can have multiple rows so if you want the buttons in different rows then just add more row {}

            primary { //A primary button
                id = "test" //Id for identifying your button in the ButtonClickEvent
                label = "Test!"

                action { e -> //This is ran when the button is clicked (only possibly if you passed the jda instance in the builder
                    e.reply("Hi!").queue()
                }
            }

            link { //An url button just open a url in the user's browser
                url = "https://google.com"
                label = "Click here to open Google"
            }

            //There are more buttons: danger, secondary, success (what just changes the color)
        }
    })
//Then just send the message with message.build():

val channel = ...
channel.sendMessage(message.build()).queue()

(SlashCommandEvent).reply(message.build()).queue()

//etc...
```

### Create roles & guild channels in custom event manager

```kotlin

val jda = JDABuilder.createDefault("token")
    .build()

jda.on<GuildMessageReceivedEvent>() {
    it.channel.sendMessage("Received Message!").queue()

    //Create channel:

    it.guild.createTextChannel("test") {

        slowMode = 20
        topic = "Very cool text channel!"

        addMemberPermission(523405005402) {
            +Permission.MESSAGE_READ //allow permission
            -Permission.MESSAGE_WRITE //deny permission
        }

    }

    //Create role:

    it.guild.createRole {

        name = "role"
        color = Color.CYAN
        mentionable = false

        permissions {  //Add permissions to the role
            +Permission.KICK_MEMBERS
            +Permission.BAN_MEMBERS
        }

    }
}

```

### Custom events on channels, guilds

```kotlin

val guild = jda.getGuildById("guild")
val voiceChannel = guild.getVoiceChannelById("id")

voiceChannel.onMemberJoin {
    println("Member join")
}

guild.onMemberJoin {
    println("new member joined")
}

```

#### There are much more custom events!
