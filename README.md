# JDA-Kotlin-Extensions

JDA-Ktx is a package for Discord.JDA which uses Kotlin-Only features like Type-Safe Builders (for creating roles, text
channels, embeds), and it has a built-in SlashCommandHandler, Music Manager + Event Manager (with coroutines). This
package is mainly for me, but you can also contribute.

**If you have ideas, bugs or simply a question please feel free to create an issue!**

# Features

- Event Manager with Coroutines
- CommandHandler for SlashCommands
- Type Safe Builder for [slash commands](https://github.com/jan-tennert/JDA-Kotlin-Extensions#slash-commands)
  , [actions rows + buttons](https://github.com/jan-tennert/JDA-Kotlin-Extensions#buttons)
  , [roles, guild channels](https://github.com/jan-tennert/JDA-Kotlin-Extensions#create-roles--guild-channels-in-custom-event-manager)
- Music Handler (documentation to-do)
- [Await Events](https://github.com/jan-tennert/JDA-Kotlin-Extensions#await-events)
- [Music Manager]()

# ToDo

- ~~Await messages etc.~~ Done
- Finish Selection menus
- Builder for Stage Channels (?) and Threads
- Find a solution for not updating the commands on every startup

# Installation

Currently, you can only install this dependency through jitpack: https://jitpack.io/#jan-tennert/JDA-Ktx/

# Interactions

#### Slash Commands

```kotlin
val jda = JDABuilder.createDefault("token").build()
val commandHandler = jda.createCommandHandler()

jda.awaitReady()

//You can create a command through a class:

commandHandler.registerCommands(testCommand())

class testCommand : Command("hi", "Say hi to the bot") {
    override suspend fun run(
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

#### Buttons

You can add buttons easy with our type safe builder:

```kotlin
val message = MessageBuilder()
  .actionRowBuilder(jda) { //If you pass your jda instance in the builder, you can listen to button clicks directly here in the builder as shown below 
    row { //You can have multiple rows so if you want the buttons in different rows then just add more row {}

      primaryButton { //A primary button
        id = "test" //Id for identifying your button in the ButtonClickEvent
        label = "Test!"

        action { e -> //This is ran when the button is clicked (only possibly if you passed the jda instance in the builder
          e.reply("Hi!").queue()
        }
      }

      linkButton { //An url button just open a url in the user's browser
        url = "https://google.com"
        label = "Click here to open Google"
      }

      //There are more buttons: danger, secondary, success (what just changes the color)
    }
  })

//Or through message action:

channel.sendMessage("Please select an option")
  .actionRowBuilder {  //Here you don't need the jda instance because MessageAction already has it.
    row {

      primaryButton {
        id = "test"
        label = "Test!"

        action { e ->
          e.reply("Hi!").queue()
        }
      }

      linkButton {
        url = "https://google.com"
        label = "Click here to open Google"
      }
    }
  })
.queue()

//Then just send the message with message.build():

val channel = ...
channel.sendMessage(message.build()).queue()

(SlashCommandEvent).reply(message.build()).queue()

//etc...
```

#### Selection Menus

*Only works if you have the access to use them*
You can add them like the buttons but not in the same row

```kotlin
val channel = ...
channel.sendMessage()
  .actionRowBuilder {
    row { //Here can u add some buttons if you want besides the selection menu
      linkButton {
        url = "https://google.com"
        label = "Google"
      }
    }
    row {
       selectionMenu {
         id = "programming_languages"
         placeHolder = "Select your favourite Programming Languages"
         minValues = 1
         maxValues = 3
         options {
           option(label = "Java", isDefault = true, value = "java")
           option(label = "Python", value = "python")
           option(label = "Kotlin", value = "kotlin")
           option(label = "C#", value = "csharp")
         }

         action {
           val selectedLanguages = it.component!!.options
           it.reply("Selected Languages: ${selectedLanguages.joinToString() { v -> v.label }}").queue()
         }
      }
    }
  }
```

### Music

You can really easy play yt videos, twitch streams, soundcloud etc.

```kotlin
val voiceChannel = ...

voiceChannel.play("https://www.youtube.com/watch?v=dQw4w9WgXcQ") //This is it and you can queue them by just reuse the play method

val guild = ...
val musicManager = guild.musicManager

//To skip, stop etc use the musicManager above you can just skip like that:
musicManager.skip()
```

You can also listen to events

```kotlin
val jda = ...
jda.on<AudioTrackPlayEvent>() {
    //do stuff
}
```

### Await Events

With await events you can wait for events without adding a listener!

```kotlin
//to wait for an event you have to be in a suspension function. If you use my SlashCommands, you should be able to use it without any scope! But if you don't use GlobalScope.launch {}

val event = jda.awaitEvent<GuildMessageReceivedEvent>() { !it.author.isBot } //Add a predicate 
println(event.message.contentRaw)

//Or you can wait for a message directly in a channel
val channel = ...
val message = channel.awaitMessage() { it.author.id == myUserId }
println(message.contentRaw)

//You can also add a timeout, that makes the result nullable
val message = channel.awaitMessage(timeout = 2000) { it.author.id == myUserId }
if (message != null) {
    println("Got message!")
} else {
    println("Time is over!")
}

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
