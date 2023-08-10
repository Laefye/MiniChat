![](https://github.com/Laefye/MiniChat/blob/main/logo.png?raw=true)
## Simple chat plugin for Paper servers
The plugin may not be stable

What is the advantage of the plugin:
- Support for multiple chat (global and local)
- Message is composed using segments (see below)
- Minimal unnecessary functionality

The plugin has support with Vault and PlaceholderAPI

## Contribute

I will be very happy if someone will help fix and introduce new features to this plugin.

[GitHub](https://github.com/Laefye/MiniChat)

## Configuration

It's very easy

config.yml:
```yaml
chats:
  # to read messages, player need have `minichat.chat.[chat name]` permission
  # to write message, player need have `minichat.chat.[chat name].write` permisssion
  local:
    range: 10 # Maximal distance (-1 is everywhere)
    world: '#current' # `#current` is one world, `#all` every world
    default: true # Is default chat?
    segments:
      -
        # MiniMessage support and PlaceholderAPI
        format: '<#438df2>{prefix}{player}{suffix} </#438df2>'
        lore: # When player hover
          - '{player} in local chat'
          - 'TPS %spark_tps%'
        command: '/msg {player}' # Suggest command when click
      - format: '-> {message}'
  global:
    range: -1
    prefix: '!' # Prefix
    world: '#all'
    segments:
      - format: '[G]'
      - format: ' {prefix}{player}{suffix} '
      - format: '-> {message}'
lang:
  permission: "<#ff0000>You don't have permissions</#ff0000>"
```