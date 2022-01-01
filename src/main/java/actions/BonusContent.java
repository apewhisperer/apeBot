package actions;

import java.util.Random;

public class BonusContent {

    static String wildMagicSurge() {

        Log.registerEvent("!surge");
        Random random = new Random();
        int roll = random.nextInt(100) + 1;

        switch (roll) {
            case 1:
            case 2:
                return "Roll on this table at the start of each of your turns for the next minute, ignoring this result on subsequent rolls.";
            case 3:
            case 4:
                return "For the next minute, you can see any invisible creature if you have line of sight to it.";
            case 5:
            case 6:
                return "A modron chosen and controlled by the DM appears in an unoccupied space within 5 feet of you, then disappears I minute later.";
            case 7:
            case 8:
                return "You cast Fireball as a 3rd-level spell centered on yourself.";
            case 9:
            case 10:
                return "You cast Magic Missile as a 5th-level spell.";
            case 11:
            case 12:
                return "Roll a d10. Your height changes by a number of inches equal to the roll. If the roll is odd, you shrink. If the roll is even, you grow.";
            case 13:
            case 14:
                return "You cast Confusion centered on yourself.";
            case 15:
            case 16:
                return "For the next minute, you regain 5 hit points at the start of each of your turns.";
            case 17:
            case 18:
                return "You grow a long beard made of feathers that remains until you sneeze, at which point the feathers explode out from your face.";
            case 19:
            case 20:
                return "You cast Grease centered on yourself.";
            case 21:
            case 22:
                return "Creatures have disadvantage on saving throws against the next spell you cast in the next minute that involves a saving throw.";
            case 23:
            case 24:
                return "Your skin turns a vibrant shade of blue. A Remove Curse spell can end this effect.";
            case 25:
            case 26:
                return "An eye appears on your forehead for the next minute. During that time, you have advantage on Wisdom (Perception) checks that rely on sight.";
            case 27:
            case 28:
                return "For the next minute, all your spells with a casting time of 1 action have a casting time of 1 bonus action.";
            case 29:
            case 30:
                return "You teleport up to 60 feet to an unoccupied space of your choice that you can see.";
            case 31:
            case 32:
                return "You are transported to the Astral Plane until the end of your next turn, after which time you return to the space you previously occupied or the nearest unoccupied space if that space is occupied.";
            case 33:
            case 34:
                return "Maximize the damage of the next damaging spell you cast within the next minute.";
            case 35:
            case 36:
                return "Roll a d10. Your age changes by a number of years equal to the roll. If the roll is odd, you get younger (minimum 1 year old). If the roll is even, you get older.";
            case 37:
            case 38:
                return "1d6 flumphs controlled by the DM appear in unoccupied spaces within 60 feet of you and are frightened of you. They vanish after 1 minute.";
            case 39:
            case 40:
                return "You regain 2d10 hit points.";
            case 41:
            case 42:
                return "You turn into a potted plant until the start of your next turn. While a plant, you are incapacitated and have vulnerability to all damage. If you drop to 0 hit points, your pot breaks, and your form reverts.";
            case 43:
            case 44:
                return "For the next minute, you can teleport up to 20 feet as a bonus action on each of your turns.";
            case 45:
            case 46:
                return "You cast Levitate on yourself.";
            case 47:
            case 48:
                return "A unicorn controlled by the DM appears in a space within 5 feet of you, then disappears 1 minute later.";
            case 49:
            case 50:
                return "You can't speak for the next minute. Whenever you try, pink bubbles float out of your mouth.";
            case 51:
            case 52:
                return "A spectral shield hovers near you for the next minute, granting you a +2 bonus to AC and immunity to Magic Missile.";
            case 53:
            case 54:
                return "You are immune to being intoxicated by alcohol for the next 5d6 days.";
            case 55:
            case 56:
                return "Your hair falls out but grows back within 24 hours.";
            case 57:
            case 58:
                return "For the next minute, any flammable object you touch that isn't being worn or carried by another creature bursts into flame.";
            case 59:
            case 60:
                return "You regain your lowest-level expended spell slot.";
            case 61:
            case 62:
                return "For the next minute, you must shout when you speak.";
            case 63:
            case 64:
                return "You cast Fog Cloud centered on yourself.";
            case 65:
            case 66:
                return "Up to three creatures you choose within 30 feet of you take 4d10 lightning damage.";
            case 67:
            case 68:
                return "You are frightened by the nearest creature until the end of your next turn.";
            case 69:
            case 70:
                return "Each creature within 30 feet of you becomes invisible for the next minute. The invisibility ends on a creature when it attacks or casts a spell.";
            case 71:
            case 72:
                return "You gain resistance to all damage for the next minute.";
            case 73:
            case 74:
                return "A random creature within 60 feet of you becomes poisoned for 1d4 hours.";
            case 75:
            case 76:
                return "You glow with bright light in a 30-foot radius for the next minute. Any creature that ends its turn within 5 feet of you is blinded until the end of its next turn.";
            case 77:
            case 78:
                return "You cast Polymorph on yourself. If you fail the saving throw, you turn into a sheep for the spell's duration.";
            case 79:
            case 80:
                return "Illusory butterflies and flower petals flutter in the air within 10 feet of you for the next minute.";
            case 81:
            case 82:
                return "You can take one additional action immediately.";
            case 83:
            case 84:
                return "Each creature within 30 feet of you takes 1d10 necrotic damage. You regain hit points equal to the sum of the necrotic damage dealt.";
            case 85:
            case 86:
                return "You cast Mirror Image.";
            case 87:
            case 88:
                return "You cast Fly on a random creature within 60 feet of you.";
            case 89:
            case 90:
                return "You become invisible for the next minute. During that time, other creatures can't hear you. The invisibility ends if you attack or cast a spell.";
            case 91:
            case 92:
                return "If you die within the next minute, you immediately come back to life as if by the Reincarnate spell.";
            case 93:
            case 94:
                return "Your size increases by one size category for the next minute.";
            case 95:
            case 96:
                return "You and all creatures within 30 feet of you gain vulnerability to piercing damage for the next minute.";
            case 97:
            case 98:
                return "You are surrounded by faint, ethereal music for the next minute.";
            case 99:
            case 100:
                return "You regain all expended sorcery points.";
        }
        return "something went bad";
    }
}