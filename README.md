# DRAGONS SURVIVAL

1. ■ Patreon - https://www.patreon.com/blackaures
2. ■ Сurseforge (download) - https://www.curseforge.com/minecraft/mc-mods/dragons-survival
3. ■ Discord Server - http://discord.gg/blackaures
4. ■ Youtube - https://www.youtube.com/channel/UCasTlG0nSSQPPvk4mrmoWLw
5. ■ Github - https://github.com/DragonSurvivalTeam/DragonSurvival
6. ■ Twitter - https://twitter.com/BlackAures
7. ■  VK  - https://vk.com/dragonssurvival

# Wiki
1. ■ RU: https://vizhivanie-drakonov.fandom.com/ru/wiki/Выживание_Драконов_Вики
2. ■ ZH:  https://dragons-survival.fandom.com/zh/wiki/龙之生存_Wiki
3. ■ ENG: https://dragons-survival.fandom.com/wiki/Dragon%27s_Survival_Wiki

# Contribution rules

1. No improvisation is allowed, unless approved by BlackAures#7759.
2. Before making pull requests, make sure that your changes work on a servers.
3. Don't break the code hot-swap ability of the IDE. This can happen if you do something in a wrong way
in object registration event handlers.
   
4. If the code works, don't fix it or improve it. 
5. If changes are necessary but could affect gameplay, ask Black Aures about this changes.
6. If something can be done without mixins, it should be done without mixins.
7. Try to keep the code clean and compatible with other mods. 
8. Don't forget to leave hints for other programmers to make it easier to work as a team and train new people. 
9. TO DO can be found on the discord server in "help-to-do".
10. Remember that this mod should not affect players who remain human. 

## Capability synchronization

General: done in event handlers, primarily with the use of `SynchronizeDragonCap` packet.

Multiplayer: handled in `SynchronizationController`.
