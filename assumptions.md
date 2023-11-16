 - The game automatically pauses when the buy menu is automatically opened upon reaching the Hero's castle, and closing this menu does not automatically unpause the game.

 - As there is no cap specified on the number of allied soldiers, the maximum amount of allied soldiers will be determined by the maximum that can be successfully displayed on the UI. Any soldiers in excess of this limit will cause the oldest soldier to leave the party (be removed) and will reward the player with a set amount of gold and experience.

 - The critical bite debuff does not stack i.e. if the character recieves two critical bites that are both active at the same time, the extra damage they recieve from vampire attacks will be the same as if they only had one critical bite debuff.

 - All allies (including towers) attack followed by all enemies, with the character attacking first.

 - Loot from enemies is only added at the end of a fight (instead of whenever an enemy dies), as it would not make sense to loot a corpse in the middle of combat.

 - The player recieves the same set amount of gold, experience, and equipment from replacing the oldest card. The same would apply to replacing the oldest equipment and replacing the oldest soldier, with a different set amount for each category.

 - Health potions can be used at any time. This function will be assinged to the 'h' key. Upon pressing 'h', the oldest health potion will be used, removing it from the player's inventory.

 - Equipment can be changed at any time.

 - Critical bites have a 10% chance of occuring.

 - The chance of the staff putting an enemy into a trance is 10%.

 - A trance lasts 3 turns (i.e. they attack enemies instead of allies 3 times before ending at the start of the 4th turn and going back to attacking allies. During a trance, tranced enemies count as allies and thus attack with other allies before the enemies attack. Since the character attacks first, if an enemy is tranced, they attack enemies on the turn they are tranced on. This means that the 3 turns of the trance are the turn the trance occured and the 2 subsequent turns. The trance only ends when the 4th turn begins.

 - The goals of amassing gold are only met if the player currently has that much gold and not if they hey previously had that much gold and not if they had obtained that much gold in total (i.e. not including gold that had been spent).

 - Zombies being slower than other enemies means that zombies move half as much as other enemies i.e. every second time the enemies move, zombies do not move.

 - Vampires running away from campfires means that vampires will not move into the battle radius of campfires, and if they spawn inside the battle radius of a campfire, or a campfire is placed such that the vampire is within its battle radius, the vampire will move in the direction that gets them out of the battle radius of the campfire the quickest.

 - The reduction of vampire critical attacks of the Shield only applies to the character, with allied soldiers still having the normal 10% chance.

 - When the One Ring says it respawns the character with full health, this means that upon losing all health, the character's health is set to full immediately and remove the critical bite debuff if it is active. Whilst this means that the character will not take any extra damage from the attack that killed them (e.g. if the character is on 4 health and is hit with an attack that drains 5 health, they will die and come back on full health, not full health minus one), they can still take damage from subsequent attacks from other enemies attacking that turn.

- The player cannot have more than one One Ring. Having a One Ring in the player's inventory prevents another from being awarded to the player.

 - When the character dies, the One Ring instantly activates if the player has one. Upon activation, the One Ring then breaks, disappearing from the inventory without rewarding any gold or experience. If the one ring is the oldest piece of equipment, it will be lost and reward the player as with every other piece of equipment.

 - When an allied soldier is transformed into a zombie, their stats (i.e. health and damage) are changed to be in line with that of a normal zombie. The replacing zombie will have full health, regardless of how much health the soldier was on when they turned.

 - A critical bite from a zombie against the character will just deal 50% more damage than normal (perhaps the character is immune, but is damaged fighting off the infection).

 - The description for the shield which states "Defends against enemy attacks" means that the damage taken from attacks is reduced, and does not refer to the reduction of the chance of critical bites from vampires that the shield also provides.

 - When the character is in the battle radius of an enemy, all entity movement on the map is paused to allow the battle to happen.

 - If, during the process of a tick, the character enters the battle radius of an enemy, that fight will begin in the next tick.

 - Enemy battle radius is defined as tile length upwards, downwards, leftwards, and right wards, i.e. not diagonally or in a circle shape.

 - Slugs and Vampires move every tick (assuming they are not in battle), and have a random chance of moving forwards, or backwards.

 - Zombies move every second tick (assuming they are not in battle), and also have a random chance of moving forwards, or backwards.

 - There is an overall cap on the amount of enemies present, which will be made according to the size of the world, and the amount of path tiles.