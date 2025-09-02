package io.github.omriberger;

import java.util.HashMap;
import java.util.Map;

public class RoomLookup {

    public static class Room {
        private final int id;
        private final String number;
        private final String name;

        public Room(int id, String number, String name) {
            this.id = id;
            this.number = number;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return number.isEmpty() ? name : number + " " + name;
        }
    }

    private static final Map<Integer, Room> ROOM_MAP = new HashMap<>();

    static {
        ROOM_MAP.put(3000, new Room(3000, "40", "מחשבים"));
        ROOM_MAP.put(3001, new Room(3001, "327", "תרפיה אומנות"));
        ROOM_MAP.put(3002, new Room(3002, "10", "מחשבים"));
        ROOM_MAP.put(3003, new Room(3003, "71", "מעבדה גדולה"));
        ROOM_MAP.put(3004, new Room(3004, "72", "מעבדה"));
        ROOM_MAP.put(3005, new Room(3005, "94", "סדנת עיצוב"));
        ROOM_MAP.put(3006, new Room(3006, "96", "סדנת אומנות"));
        ROOM_MAP.put(3007, new Room(3007, "95", "תולדות האומנות"));
        ROOM_MAP.put(3008, new Room(3008, "", "מחול"));
        ROOM_MAP.put(3009, new Room(3009, "93", "חדר מוסיקה"));
        ROOM_MAP.put(3010, new Room(3010, "26", "יב9"));
        ROOM_MAP.put(3011, new Room(3011, "83", ""));
        ROOM_MAP.put(3012, new Room(3012, "", "ביולוגיה"));
        ROOM_MAP.put(3013, new Room(3013, "", "כימיה 84"));
        ROOM_MAP.put(3014, new Room(3014, "", "פיזיקה 85"));
        ROOM_MAP.put(3015, new Room(3015, "122", "תרפיה"));
        ROOM_MAP.put(3016, new Room(3016, "", "מגרש"));
        ROOM_MAP.put(3019, new Room(3019, "47", "חדר הקבצה"));
        ROOM_MAP.put(3022, new Room(3022, "34", "י7"));
        ROOM_MAP.put(3024, new Room(3024, "54", ""));
        ROOM_MAP.put(3025, new Room(3025, "65", "לבנה"));
        ROOM_MAP.put(3027, new Room(3027, "13", "יא2"));
        ROOM_MAP.put(3028, new Room(3028, "11", "יא3"));
        ROOM_MAP.put(3029, new Room(3029, "15", "יא5"));
        ROOM_MAP.put(3030, new Room(3030, "16", "יא6"));
        ROOM_MAP.put(3031, new Room(3031, "22", "יא7"));
        ROOM_MAP.put(3032, new Room(3032, "14", "יא1"));
        ROOM_MAP.put(3033, new Room(3033, "12", "יא4"));
        ROOM_MAP.put(3034, new Room(3034, "106", "ח5 שמע"));
        ROOM_MAP.put(3035, new Room(3035, "105", "ח1"));
        ROOM_MAP.put(3036, new Room(3036, "102", "ח2 שמע"));
        ROOM_MAP.put(3037, new Room(3037, "112", "ח3"));
        ROOM_MAP.put(3039, new Room(3039, "116", "ח7 שמע"));
        ROOM_MAP.put(3040, new Room(3040, "104", "ח6"));
        ROOM_MAP.put(3042, new Room(3042, "41", "י1"));
        ROOM_MAP.put(3043, new Room(3043, "42", "י2"));
        ROOM_MAP.put(3044, new Room(3044, "35", "י6"));
        ROOM_MAP.put(3045, new Room(3045, "36", "י5"));
        ROOM_MAP.put(3046, new Room(3046, "44", "י4"));
        ROOM_MAP.put(3047, new Room(3047, "43", "י3"));
        ROOM_MAP.put(3049, new Room(3049, "", "מהות חטע"));
        ROOM_MAP.put(3050, new Room(3050, "64", "ז4"));
        ROOM_MAP.put(3052, new Room(3052, "56", "ז3"));
        ROOM_MAP.put(3054, new Room(3054, "", "מהות חטב 1"));
        ROOM_MAP.put(3055, new Room(3055, "103", "ח4"));
        ROOM_MAP.put(3058, new Room(3058, "33", "יב4"));
        ROOM_MAP.put(3059, new Room(3059, "63", "ז6"));
        ROOM_MAP.put(3060, new Room(3060, "61", "ז1"));
        ROOM_MAP.put(3061, new Room(3061, "53", "ז2 שמע"));
        ROOM_MAP.put(3062, new Room(3062, "51", "ז7"));
        ROOM_MAP.put(3063, new Room(3063, "121", "ט3"));
        ROOM_MAP.put(3065, new Room(3065, "114", "ט5"));
        ROOM_MAP.put(3066, new Room(3066, "115", "ט1"));
        ROOM_MAP.put(3068, new Room(3068, "120", "ט4"));
        ROOM_MAP.put(3069, new Room(3069, "55", "ז5"));
        ROOM_MAP.put(3070, new Room(3070, "113", "ט6"));
        ROOM_MAP.put(3072, new Room(3072, "32", "יב2"));
        ROOM_MAP.put(3074, new Room(3074, "", "107 (שילוב)"));
        ROOM_MAP.put(3075, new Room(3075, "", "מקלט ניצנים"));
        ROOM_MAP.put(3076, new Room(3076, "23", "יב5"));
        ROOM_MAP.put(3077, new Room(3077, "31", "יב1"));
        ROOM_MAP.put(3079, new Room(3079, "24", "יב6"));
        ROOM_MAP.put(3080, new Room(3080, "95", "יב7"));
        ROOM_MAP.put(3081, new Room(3081, "25", "יב3"));
        ROOM_MAP.put(3082, new Room(3082, "85", "מעבדה פיזיקה"));
        ROOM_MAP.put(3083, new Room(3083, "25", ""));
        ROOM_MAP.put(3084, new Room(3084, "124", "ט2"));
        ROOM_MAP.put(3085, new Room(3085, "80", "מעבדה ביולוגיה"));
        ROOM_MAP.put(3086, new Room(3086, "82", "יב8"));
        ROOM_MAP.put(3087, new Room(3087, "21", "יא8"));
        ROOM_MAP.put(3088, new Room(3088, "125", "מטבח"));
        ROOM_MAP.put(3089, new Room(3089, "", "חדר במדרגות"));
        ROOM_MAP.put(3090, new Room(3090, "30", ""));
        ROOM_MAP.put(3091, new Room(3091, "101", "ח8"));
        ROOM_MAP.put(3092, new Room(3092, "66", "ז8 (שמע)"));
        ROOM_MAP.put(3093, new Room(3093, "111", "ט8"));
        ROOM_MAP.put(3094, new Room(3094, "", "אודיטוריום"));
        ROOM_MAP.put(3095, new Room(3095, "", "חדר תרפיה (אומנויות)"));
        ROOM_MAP.put(3096, new Room(3096, "", "חדר רדיו"));
        ROOM_MAP.put(3097, new Room(3097, "46", "י8"));
        ROOM_MAP.put(3098, new Room(3098, "", "68 אופק/עוז"));
        ROOM_MAP.put(3099, new Room(3099, "123", "ט7"));
        ROOM_MAP.put(3100, new Room(3100, "", "חדר מהות -יא10 יב10"));
        ROOM_MAP.put(3101, new Room(3101, "", "חדר מוסיקה"));
        ROOM_MAP.put(3102, new Room(3102, "73", "מעבדה גדולה"));
        ROOM_MAP.put(3103, new Room(3103, "74", "מעבדה"));
        ROOM_MAP.put(3104, new Room(3104, "", "מגרש תום שוהם"));
        ROOM_MAP.put(3105, new Room(3105, "", "אולם ספורט"));
        ROOM_MAP.put(3106, new Room(3106, "45", "י9"));
        ROOM_MAP.put(3107, new Room(3107, "126-7", "מדעית הנדסית"));
        ROOM_MAP.put(3108, new Room(3108, "30", "מחשבים"));
        ROOM_MAP.put(3109, new Room(3109, "52", ""));
        ROOM_MAP.put(3110, new Room(3110, "91א", ""));
        ROOM_MAP.put(3111, new Room(3111, "64", ""));
        ROOM_MAP.put(3112, new Room(3112, "50", "מחשבים"));
        ROOM_MAP.put(3113, new Room(3113, "91ב", ""));
        ROOM_MAP.put(3114, new Room(3114, "91ג", ""));
        ROOM_MAP.put(3119, new Room(3119, "30", "מעבדת מחשבים"));
        ROOM_MAP.put(3124, new Room(3124, "18", "חנ``ג"));
        ROOM_MAP.put(3167, new Room(3167, "20", "מחשבים"));
        ROOM_MAP.put(3985, new Room(3985, "58", "רוית וסיגל"));
        ROOM_MAP.put(3986, new Room(3986, "57", "יועצת"));
        ROOM_MAP.put(3987, new Room(3987, "100", "רכזת"));
        ROOM_MAP.put(3988, new Room(3988, "108", "איריס"));
        ROOM_MAP.put(3989, new Room(3989, "110", "יועצת"));
        ROOM_MAP.put(3990, new Room(3990, "128", "רכזת"));
        ROOM_MAP.put(3991, new Room(3991, "117", "יועצת"));
        ROOM_MAP.put(3992, new Room(3992, "67", "רכזת"));
        ROOM_MAP.put(3993, new Room(3993, "", "מהות חטב 3"));
        ROOM_MAP.put(3994, new Room(3994, "", "מהות חטב 2"));
        ROOM_MAP.put(3995, new Room(3995, "90", "חדר פלא"));
        ROOM_MAP.put(3996, new Room(3996, "86", "חדר תיאטרון"));
        ROOM_MAP.put(3997, new Room(3997, "62", ""));
        ROOM_MAP.put(3998, new Room(3998, "84", "מעבדה כימיה"));
        ROOM_MAP.put(3999, new Room(3999, "60", "אמנות ונגריה"));
    }

    public static Room getRoom(int roomId) {
        return ROOM_MAP.get(roomId);
    }
}
