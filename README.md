# DRAGONS SURVIVAL

1. ■ Patreon - https://www.patreon.com/blackaures
2. ■ Сurseforge (download) - https://www.curseforge.com/minecraft/mc-mods/dragons-survival
3. ■ Discord Server - http://discord.gg/8SsB8ar
4. ■ Youtube - https://www.youtube.com/channel/UCasTlG0nSSQPPvk4mrmoWLw
5. ■ Github - https://github.com/DragonSurvivalTeam/DragonSurvival
6. ■ Twitter - https://twitter.com/BlackAures
7. ■  VK  - https://vk.com/dragonssurvival

# Wiki
1. ■ RU: https://vizhivanie-drakonov.fandom.com/ru/wiki/Выживание_Драконов_Вики
2. ■ ZH:  https://dragons-survival.fandom.com/zh/wiki/龙之生存_Wiki
3. ■ ENG: https://dragons-survival.fandom.com/wiki/Dragon%27s_Survival_Wiki

# Project Description
■ This project is creating a global modification that adds the ability to turn into a dragon and all the content associated with it. Details about the project are on curseforge and on discord. 

- ■ *I've always liked dragons and this wonderful game, but I've been playing alone and without modifications my whole life, until I accidentally came across this cultural phenomenon in 2020. I was fascinated by the amount of player-created content. There were so many mods created by them, many of which had to do with dragons. You could tame them, breed them, ride them, kill them, but in none of them could you **BE a dragon**. This really upset me. Being an artist very far from programming, modding and the minecraft community, I decided to take a risk and create my own mod. I had absolutely no knowledge or friends in this field. The only thing I had was a dream and the support of a community of dragon lovers. That's about how I became the developer of Dragons Survival.* 

- ■ *I hope that with time we'll solve all the old problems caused by inexperience, improve the graphical style and add everything we have planned. All of this is taking a huge amount of time. So far, the biggest problem for development is the lack of stable programming. I have to hire outside people and pay them for their work. At the moment the entire mod cost me about 950$ and this figure is growing rapidly. Most of the graphic component is done by myself without any financial gain. All of Patreon's money goes to development. But the financial problem is the smallest of the problems. A much bigger problem is my lack of any knowledge of java. If anyone would like to help with the code, I'd be really happy to do it and willing to pay for the work.*

(c) **Black Aures**

# Contribution rules
■  You are free to participate in the development of the project, but keep in mind that the **owner is an artist** and does not understand how the code works. For this reason, we have terrible things going on here, but we are gradually solving these problems. If you want to be rewarded for your code - write **BlackAures#7759**
____
■  I welcome any help and would be very happy if you decided to participate. A few **small rules** to make it easier for us to work and for you not to do the work for nothing. 

**1.** If you want to change something globally, **check with me** if this is worth it. I am very sensitive to player expectations and if your suggestion could cause a negative gameplay experience, I will probably say no.

**2.** Before sending pull requests, make sure it **works on a servers.**

**3.** If you want to improve something that works, **evaluate your strengths**. It would be very bad if you leave me with broken code and I can't do anything about it. For this reason, I am wary of any global changes. 

**4.** Try to keep as much **compatibility** with other mods and **flexibility in customization** as possible.

**5.** The detailed **TO DO list** is only available from BlackAures. The short version is contained in Discord and on Patreon. Ask me to send it to you.

**6.** Don't forget to leave **hints for other programmers** to make it easier to work as a team and train new people. There are a lot of different people working on the project and it's really hard to figure out what's going on with us. 

**7.** This mod should **not affect for a humans gameplay.**

**8.** Remember that **skins cannot be broken** in any way. We have so many people who have created their own skins and some have paid money to get them. It would be very unfortunate if people were to lose them in any way or have to fix something in their skins. We will need to make an additional skinning system in the future which will be automated. This system should be completely safe, have the ability to temporarily disable your skin within the game and be tied to a certain kind of dragon (for example, *BlackAures_adult* will replace all adult dragons, and *BlackAures_aduld_sea* will replace only adult sea dragons). The current system allows you to use the skin offline, it's important to keep this.
____
■  **A few more important details to keep in mind when adding new content:**
- player opinions are important to me and we often have polls when there are controversial issues;
- dragons in people's imagination can be very diverse, but I want us to have only 1 perfect model with different texture variations;
- I don't want to add new types of dragons yet, but at some point it may happen (without changing the model);
- magic predators and nests will be completely redone;
- this mod is magic, there should not be any technical components;
- we need to keep a balance between dragons and humans. Dragons shouldn't be omnipotent;
- a lot of content is planned, but we do not publish details. This is available to developers only;
- flight of dragons should remain swift, but it should be improved;
- dragons fly and control objects with levitation and telekinesis, there is no need to add them in the mouth or anywhere else;
- the developer is Russian-speaking, do not be afraid to make edits to english translation and help with this.
____
## Capability synchronization

General: done in event handlers, primarily with the use of `SynchronizeDragonCap` packet.

Multiplayer: handled in `SynchronizationController`.
____
