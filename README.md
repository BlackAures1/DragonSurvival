# "Dragon survival" mod for Minecraft

This mod lets you play as a dragon.

# Contribution rules

1. No improvisation is allowed, unless approved by Black Aures.
2. Before making pull requests, make sure that your changes work on the server.
3. Don't break the code hot-swap ability of the IDE. This can happen if you do something in a wrong way
in object registration event handlers.
   
4. If the code works, don't fix it or improve it.
5. If something can be done without mixins, it should be done without mixins.

## Capability synchronization

General: done in event handlers, primarily with the use of `SynchronizeDragonCap` packet.

Multiplayer: handled in `SynchronizationController`.