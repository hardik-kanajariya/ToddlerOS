package com.toddleros.core.media

import androidx.compose.ui.graphics.Color
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelTeal
import com.toddleros.core.theme.PastelTealDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark

data class AnimalAsset(
    val id: String,
    val name: String,
    val emoji: String,
    val soundFile: String,
    val imageFile: String?,
    val group: String,
    val cardColor: Color,
    val textColor: Color
)

data class SongAsset(
    val id: String,
    val title: String,
    val emoji: String,
    val audioFile: String,
    val description: String,
    val cardColor: Color,
    val textColor: Color
)

data class StoryAsset(
    val id: String,
    val title: String,
    val emoji: String,
    val markdownFile: String,
    val summary: String,
    val cardColor: Color,
    val textColor: Color
)

data class VideoAsset(
    val id: String,
    val title: String,
    val emoji: String,
    val videoFile: String,
    val description: String,
    val cardColor: Color,
    val textColor: Color
)

data class ImageItemAsset(
    val id: String,
    val label: String,
    val svgPath: String,
    val category: String
)

object MediaAssetRegistry {

    val animals: List<AnimalAsset> = listOf(
        // Farm
        AnimalAsset("cow", "Cow", "🐮", "animal-sounds/farm/cow-moo.ogg", "educational-images/animals/cow.svg", "Farm", PastelYellow, PastelYellowDark),
        AnimalAsset("sheep", "Sheep", "🐑", "animal-sounds/farm/sheep-bleat.ogg", "educational-images/animals/sheep.svg", "Farm", PastelMint, PastelMintDark),
        AnimalAsset("duck", "Duck", "🦆", "animal-sounds/farm/duck-quack.wav", "educational-images/animals/duck.svg", "Farm", PastelSky, PastelSkyDark),
        AnimalAsset("pig", "Pig", "🐷", "animal-sounds/farm/pig-oink.ogg", "educational-images/animals/pig.svg", "Farm", PastelCoral, PastelCoralDark),
        AnimalAsset("chicken", "Chicken", "🐔", "animal-sounds/farm/chicken-cluck.ogg", null, "Farm", PastelLavender, PastelLavenderDark),
        AnimalAsset("rooster", "Rooster", "🐓", "animal-sounds/farm/rooster-crow.ogg", null, "Farm", PastelPeach, PastelPeachDark),
        AnimalAsset("donkey", "Donkey", "🫏", "animal-sounds/farm/donkey-bray.wav", null, "Farm", PastelTeal, PastelTealDark),
        AnimalAsset("goat", "Goat", "🐐", "animal-sounds/farm/goat-bleat.ogg", null, "Farm", PastelYellow, PastelYellowDark),

        // Pets
        AnimalAsset("dog", "Dog", "🐶", "animal-sounds/pets/dog-bark.ogg", "educational-images/animals/dog.svg", "Pets", PastelCoral, PastelCoralDark),
        AnimalAsset("cat", "Cat", "🐱", "animal-sounds/pets/cat-meow.ogg", "educational-images/animals/cat.svg", "Pets", PastelLavender, PastelLavenderDark),
        AnimalAsset("frog", "Frog", "🐸", "animal-sounds/pets/frog-croak.flac", "educational-images/animals/frog.svg", "Pets", PastelMint, PastelMintDark),
        AnimalAsset("rabbit", "Rabbit", "🐰", "animal-sounds/pets/rabbit-purr.flac", null, "Pets", PastelPeach, PastelPeachDark),
        AnimalAsset("bee", "Bumblebee", "🐝", "animal-sounds/pets/bee-buzz.opus", null, "Pets", PastelYellow, PastelYellowDark),
        AnimalAsset("dolphin", "Dolphin", "🐬", "animal-sounds/pets/dolphin-clicks.wav", null, "Pets", PastelSky, PastelSkyDark),
        AnimalAsset("whale", "Whale", "🐋", "animal-sounds/pets/whale-song.ogg", null, "Pets", PastelTeal, PastelTealDark),
        AnimalAsset("cricket", "Cricket", "🦗", "animal-sounds/pets/cricket-chirp.oga", null, "Pets", PastelMint, PastelMintDark),

        // Wild
        AnimalAsset("lion", "Lion", "🦁", "animal-sounds/farm/cow-moo.ogg", "educational-images/animals/lion.svg", "Wild", PastelYellow, PastelYellowDark),
        AnimalAsset("bear", "Bear", "🐻", "animal-sounds/wild/bear-roar.mp3", "educational-images/animals/bear.svg", "Wild", PastelCoral, PastelCoralDark),
        AnimalAsset("monkey", "Monkey", "🐵", "animal-sounds/wild/monkey-howler.ogg", "educational-images/animals/monkey.svg", "Wild", PastelPeach, PastelPeachDark),
        AnimalAsset("elephant", "Elephant", "🐘", "animal-sounds/farm/donkey-bray.wav", "educational-images/animals/elephant.svg", "Wild", PastelSky, PastelSkyDark),
        AnimalAsset("wolf", "Wolf", "🐺", "animal-sounds/wild/wolf-howl.ogg", null, "Wild", PastelLavender, PastelLavenderDark),
        AnimalAsset("fox", "Fox", "🦊", "animal-sounds/wild/fox-bark.ogg", null, "Wild", PastelCoral, PastelCoralDark),
        AnimalAsset("deer", "Deer", "🦌", "animal-sounds/wild/deer-growl.ogg", null, "Wild", PastelTeal, PastelTealDark),
        AnimalAsset("zebra", "Zebra", "🦓", "animal-sounds/wild/zebra-call.ogg", null, "Wild", PastelMint, PastelMintDark),

        // Birds
        AnimalAsset("parrot", "Parrot", "🦜", "animal-sounds/birds/parrot-call.wav", null, "Birds", PastelCoral, PastelCoralDark),
        AnimalAsset("cuckoo", "Cuckoo", "🐦", "animal-sounds/birds/cuckoo-call.ogg", null, "Birds", PastelMint, PastelMintDark),
        AnimalAsset("owl", "Owl", "🦉", "animal-sounds/birds/owl-hoot.mp3", null, "Birds", PastelLavender, PastelLavenderDark),
        AnimalAsset("eagle", "Eagle", "🦅", "animal-sounds/birds/eagle-screech.mp3", null, "Birds", PastelYellow, PastelYellowDark),
        AnimalAsset("peacock", "Peacock", "🦚", "animal-sounds/birds/peacock-call.ogg", null, "Birds", PastelTeal, PastelTealDark),
        AnimalAsset("pigeon", "Pigeon", "🕊️", "animal-sounds/birds/pigeon-coo.mp3", null, "Birds", PastelPeach, PastelPeachDark),
        AnimalAsset("crow", "Crow", "🐦‍⬛", "animal-sounds/birds/crow-caw.mp3", null, "Birds", PastelSky, PastelSkyDark)
    )

    val songs: List<SongAsset> = listOf(
        SongAsset("twinkle", "Twinkle Twinkle Little Star", "⭐", "nursery-rhymes/twinkle-twinkle-little-star.ogg", "Gentle starry lullaby", PastelSky, PastelSkyDark),
        SongAsset("old_macdonald", "Old MacDonald Had a Farm", "🚜", "nursery-rhymes/old-macdonald-had-a-farm.ogg", "Joyful farm animals", PastelYellow, PastelYellowDark),
        SongAsset("row_boat", "Row Row Row Your Boat", "⛵", "nursery-rhymes/row-row-row-your-boat.ogg", "Gentle stream song", PastelMint, PastelMintDark),
        SongAsset("alphabet_song", "The Alphabet Song (ABC)", "🔤", "nursery-rhymes/alphabet-song.ogg", "Sing your letters A to Z", PastelCoral, PastelCoralDark),
        SongAsset("baa_baa", "Baa Baa Black Sheep", "🐑", "nursery-rhymes/baa-baa-black-sheep.ogg", "Playful woolly sheep", PastelLavender, PastelLavenderDark),
        SongAsset("itsy_spider", "Itsy Bitsy Spider", "🕷️", "nursery-rhymes/itsy-bitsy-spider.ogg", "Up the water spout", PastelPeach, PastelPeachDark),
        SongAsset("london_bridge", "London Bridge Is Falling Down", "🌉", "nursery-rhymes/london-bridge-is-falling-down.ogg", "Classic historic rhyme", PastelTeal, PastelTealDark),
        SongAsset("mary_lamb", "Mary Had a Little Lamb", "🤍", "nursery-rhymes/mary-had-a-little-lamb.ogg", "White as snow", PastelYellow, PastelYellowDark),
        SongAsset("humpty", "Humpty Dumpty", "🥚", "nursery-rhymes/humpty-dumpty.ogg", "Sat on a wall", PastelSky, PastelSkyDark),
        SongAsset("jack_jill", "Jack and Jill", "🪣", "nursery-rhymes/jack-and-jill.ogg", "Up the hill", PastelMint, PastelMintDark),
        SongAsset("brahms", "Brahms' Lullaby Music Box", "🎶", "nursery-rhymes/brahms-lullaby-music-box.ogg", "Sweet bedtime music box", PastelLavender, PastelLavenderDark),
        SongAsset("be_thankful", "Be Thankful Song", "💖", "educational-songs/be-thankful-song.ogg", "Kindness and gratitude", PastelCoral, PastelCoralDark),
        SongAsset("mother_goose_band", "Sousa's Mother Goose Band", "🎺", "educational-songs/sousa-mother-goose-band.mp3", "Grand musical medley", PastelPeach, PastelPeachDark),
        SongAsset("this_old_man", "This Old Man", "🥁", "nursery-rhymes/childrens-theme-this-old-man.mp3", "Knick-knack paddywhack", PastelTeal, PastelTealDark),
        SongAsset("pop_weasel", "Pop Goes the Weasel", "🎈", "nursery-rhymes/pop-goes-the-weasel.ogg", "Pop goes the tune", PastelYellow, PastelYellowDark),
        SongAsset("hot_cross_buns", "Hot Cross Buns", "🥖", "nursery-rhymes/hot-cross-buns.ogg", "One a penny, two a penny", PastelMint, PastelMintDark),
        SongAsset("three_blind_mice", "Three Blind Mice", "🐭", "nursery-rhymes/three-blind-mice.ogg", "See how they run", PastelSky, PastelSkyDark),
        SongAsset("three_kittens", "Three Little Kittens", "🐱", "nursery-rhymes/three-little-kittens.ogg", "Found their mittens", PastelCoral, PastelCoralDark),
        SongAsset("yankee_doodle", "Yankee Doodle", "🐴", "nursery-rhymes/yankee-doodle.mp3", "Feather in his hat", PastelLavender, PastelLavenderDark),
        SongAsset("frere_jacques", "Frère Jacques", "🔔", "nursery-rhymes/mester-jacob-frere-jacques.ogg", "Are you sleeping?", PastelPeach, PastelPeachDark),
        SongAsset("irish_lullaby", "Too-Ra-Loo-Ra (Irish Lullaby)", "☘️", "nursery-rhymes/irish-lullaby.ogg", "Soothing traditional lullaby", PastelTeal, PastelTealDark),
        SongAsset("oh_susanna", "Oh Susanna", "🪕", "nursery-rhymes/oh-susanna.ogg", "Banjo on my knee", PastelYellow, PastelYellowDark),
        SongAsset("pease_porridge", "Pease Porridge Hot", "🥣", "nursery-rhymes/pease-porridge-hot.ogg", "Nine days old", PastelMint, PastelMintDark),
        SongAsset("ding_dong_bell", "Ding Dong Bell", "🔔", "nursery-rhymes/ding-dong-bell.ogg", "Pussy's in the well", PastelSky, PastelSkyDark)
    )

    val videos: List<VideoAsset> = listOf(
        VideoAsset("vid_twinkle", "Twinkle Twinkle Little Star", "⭐", "educational-videos/twinkle-twinkle-little-star.webm", "Sing along with the bright stars in the night sky", PastelSky, PastelSkyDark),
        VideoAsset("vid_spider", "Itsy Bitsy Spider", "🕷️", "educational-videos/insey-winsey-spider.webm", "Watch the little spider climb up the spout", PastelMint, PastelMintDark),
        VideoAsset("vid_counting", "Counting with Potsie", "🔢", "educational-videos/counting-with-potsie.webm", "Fun numbers and friendly counting with Potsie", PastelYellow, PastelYellowDark),
        VideoAsset("vid_rocket", "Zoom Zoom Zoom!", "🚀", "educational-videos/zoom-zoom-zoom.webm", "Fly high to the moon in a magical rocket ship", PastelCoral, PastelCoralDark),
        VideoAsset("vid_blackbirds", "Two Little Blackbirds", "🐦", "educational-videos/two-little-blackbirds.webm", "Peter and Paul sitting on a garden wall", PastelLavender, PastelLavenderDark),
        VideoAsset("vid_bobbin", "Wind the Bobbin Up", "🧵", "educational-videos/wind-the-bobbin-up.webm", "Clap clap clap and point to the ceiling", PastelPeach, PastelPeachDark),
        VideoAsset("vid_train", "Steam Train Station", "🚂", "educational-videos/steam-train-transportation.webm", "Watch real puffing steam engines on the tracks", PastelTeal, PastelTealDark),
        VideoAsset("vid_alphabet", "Animal Alphabet Animation", "🔤", "educational-videos/animal-alphabet-animation.ogv", "Animated animals introducing letters A to Z", PastelYellow, PastelYellowDark)
    )

    val stories: List<StoryAsset> = listOf(
        StoryAsset("story_vasu", "Vasu's Sunny Day in the Garden", "🌻", "stories/vasus-sunny-day-in-the-garden.md", "Vasu plays with birds, flowers and sunshine", PastelYellow, PastelYellowDark),
        StoryAsset("story_tortoise", "The Tortoise and the Hare", "🐢", "stories/the-tortoise-and-the-hare.md", "Slow and steady wins the race", PastelMint, PastelMintDark),
        StoryAsset("story_lion_mouse", "The Lion and the Mouse", "🐭", "stories/the-lion-and-the-mouse.md", "Even the tiniest friend can help", PastelCoral, PastelCoralDark),
        StoryAsset("story_peter_rabbit", "The Tale of Peter Rabbit", "🐰", "stories/the-tale-of-peter-rabbit.md", "Adventures in Mr. McGregor's garden", PastelSky, PastelSkyDark),
        StoryAsset("story_three_pigs", "The Three Little Pigs", "🐷", "stories/the-three-little-pigs.md", "Straw, sticks and sturdy brick houses", PastelLavender, PastelLavenderDark),
        StoryAsset("story_goldilocks", "Goldilocks & The Three Bears", "🐻", "stories/goldilocks-and-the-three-bears.md", "Bowls of porridge and cozy beds", PastelPeach, PastelPeachDark),
        StoryAsset("story_gingerbread", "The Gingerbread Man", "🍪", "stories/the-gingerbread-man.md", "Run, run as fast as you can!", PastelTeal, PastelTealDark),
        StoryAsset("story_ugly_duckling", "The Ugly Duckling", "🦢", "stories/the-ugly-duckling.md", "Growing into a beautiful graceful swan", PastelYellow, PastelYellowDark),
        StoryAsset("story_velveteen", "The Velveteen Rabbit", "🧸", "stories/the-velveteen-rabbit.md", "Becoming real through endless love", PastelMint, PastelMintDark),
        StoryAsset("story_little_hen", "The Little Red Hen", "🌾", "stories/the-little-red-hen.md", "Planting seeds and baking warm bread", PastelCoral, PastelCoralDark),
        StoryAsset("story_sun_wind", "The Wind and the Sun", "☀️", "stories/the-wind-and-the-sun.md", "Warm gentle smiles are the strongest", PastelSky, PastelSkyDark),
        StoryAsset("story_frog_prince", "The Frog Prince", "👑", "stories/the-frog-prince.md", "A golden ball and a royal pond", PastelLavender, PastelLavenderDark)
    )

    val imageCategories: Map<String, List<ImageItemAsset>> = mapOf(
        "Animals" to listOf(
            ImageItemAsset("cat", "Cat", "educational-images/animals/cat.svg", "Animals"),
            ImageItemAsset("dog", "Dog", "educational-images/animals/dog.svg", "Animals"),
            ImageItemAsset("cow", "Cow", "educational-images/animals/cow.svg", "Animals"),
            ImageItemAsset("duck", "Duck", "educational-images/animals/duck.svg", "Animals"),
            ImageItemAsset("sheep", "Sheep", "educational-images/animals/sheep.svg", "Animals"),
            ImageItemAsset("pig", "Pig", "educational-images/animals/pig.svg", "Animals"),
            ImageItemAsset("lion", "Lion", "educational-images/animals/lion.svg", "Animals"),
            ImageItemAsset("bear", "Bear", "educational-images/animals/bear.svg", "Animals"),
            ImageItemAsset("elephant", "Elephant", "educational-images/animals/elephant.svg", "Animals"),
            ImageItemAsset("monkey", "Monkey", "educational-images/animals/monkey.svg", "Animals"),
            ImageItemAsset("horse", "Horse", "educational-images/animals/horse.svg", "Animals"),
            ImageItemAsset("frog", "Frog", "educational-images/animals/frog.svg", "Animals")
        ),
        "Fruits" to listOf(
            ImageItemAsset("apple", "Apple", "educational-images/fruits/apple.svg", "Fruits"),
            ImageItemAsset("banana", "Banana", "educational-images/fruits/banana.svg", "Fruits"),
            ImageItemAsset("orange", "Orange", "educational-images/fruits/orange.svg", "Fruits"),
            ImageItemAsset("strawberry", "Strawberry", "educational-images/fruits/strawberry.svg", "Fruits"),
            ImageItemAsset("grape", "Grapes", "educational-images/fruits/grape.svg", "Fruits"),
            ImageItemAsset("watermelon", "Watermelon", "educational-images/fruits/watermelon.svg", "Fruits"),
            ImageItemAsset("carrot", "Carrot", "educational-images/fruits/carrot.svg", "Fruits"),
            ImageItemAsset("broccoli", "Broccoli", "educational-images/fruits/broccoli.svg", "Fruits"),
            ImageItemAsset("tomato", "Tomato", "educational-images/fruits/tomato.svg", "Fruits"),
            ImageItemAsset("corn", "Sweet Corn", "educational-images/fruits/corn.svg", "Fruits")
        ),
        "Alphabet" to listOf(
            ImageItemAsset("a", "A is for Apple", "educational-images/alphabet/a-apple.svg", "Alphabet"),
            ImageItemAsset("b", "B is for Ball", "educational-images/alphabet/b-ball.svg", "Alphabet"),
            ImageItemAsset("c", "C is for Cat", "educational-images/alphabet/c-cat.svg", "Alphabet"),
            ImageItemAsset("d", "D is for Dog", "educational-images/alphabet/d-dog.svg", "Alphabet"),
            ImageItemAsset("e", "E is for Elephant", "educational-images/alphabet/e-elephant.svg", "Alphabet"),
            ImageItemAsset("f", "F is for Fish", "educational-images/alphabet/f-fish.svg", "Alphabet"),
            ImageItemAsset("g", "G is for Grapes", "educational-images/alphabet/g-grapes.svg", "Alphabet"),
            ImageItemAsset("h", "H is for Hat", "educational-images/alphabet/h-hat.svg", "Alphabet"),
            ImageItemAsset("s", "S is for Sun", "educational-images/alphabet/s-sun.svg", "Alphabet"),
            ImageItemAsset("t", "T is for Train", "educational-images/alphabet/t-train.svg", "Alphabet"),
            ImageItemAsset("z", "Z is for Zebra", "educational-images/alphabet/z-zebra.svg", "Alphabet")
        ),
        "Numbers" to (1..10).map { num ->
            ImageItemAsset("num_$num", "Number $num", "educational-images/numbers/number-$num.svg", "Numbers")
        },
        "Colors" to listOf(
            ImageItemAsset("red", "Red", "educational-images/colors/color-red.svg", "Colors"),
            ImageItemAsset("blue", "Blue", "educational-images/colors/color-blue.svg", "Colors"),
            ImageItemAsset("green", "Green", "educational-images/colors/color-green.svg", "Colors"),
            ImageItemAsset("yellow", "Yellow", "educational-images/colors/color-yellow.svg", "Colors"),
            ImageItemAsset("orange", "Orange", "educational-images/colors/color-orange.svg", "Colors"),
            ImageItemAsset("purple", "Purple", "educational-images/colors/color-purple.svg", "Colors"),
            ImageItemAsset("brown", "Brown", "educational-images/colors/color-brown.svg", "Colors"),
            ImageItemAsset("white", "White", "educational-images/colors/color-white.svg", "Colors")
        ),
        "Shapes" to listOf(
            ImageItemAsset("circle", "Circle", "educational-images/shapes/circle.svg", "Shapes"),
            ImageItemAsset("square", "Square", "educational-images/shapes/square.svg", "Shapes"),
            ImageItemAsset("triangle", "Triangle", "educational-images/shapes/triangle.svg", "Shapes"),
            ImageItemAsset("star", "Star", "educational-images/shapes/star.svg", "Shapes"),
            ImageItemAsset("heart", "Heart", "educational-images/shapes/heart.svg", "Shapes"),
            ImageItemAsset("diamond", "Diamond", "educational-images/shapes/diamond.svg", "Shapes")
        ),
        "Nature" to listOf(
            ImageItemAsset("sun", "Warm Sun", "educational-images/nature/sun.svg", "Nature"),
            ImageItemAsset("cloud", "Fluffy Cloud", "educational-images/nature/cloud.svg", "Nature"),
            ImageItemAsset("rainbow", "Rainbow", "educational-images/nature/rainbow.svg", "Nature"),
            ImageItemAsset("moon", "Crescent Moon", "educational-images/nature/moon.svg", "Nature"),
            ImageItemAsset("flower", "Blooming Flower", "educational-images/nature/flower.svg", "Nature"),
            ImageItemAsset("tree", "Green Tree", "educational-images/nature/tree.svg", "Nature"),
            ImageItemAsset("rain", "Raindrops", "educational-images/nature/rain.svg", "Nature"),
            ImageItemAsset("mountain", "Mountain", "educational-images/nature/mountain.svg", "Nature")
        ),
        "Transportation" to listOf(
            ImageItemAsset("car", "Car", "educational-images/transportation/car.svg", "Transportation"),
            ImageItemAsset("bus", "School Bus", "educational-images/transportation/bus.svg", "Transportation"),
            ImageItemAsset("train", "Steam Train", "educational-images/transportation/train.svg", "Transportation"),
            ImageItemAsset("airplane", "Airplane", "educational-images/transportation/airplane.svg", "Transportation"),
            ImageItemAsset("boat", "Sailboat", "educational-images/transportation/boat.svg", "Transportation"),
            ImageItemAsset("bicycle", "Bicycle", "educational-images/transportation/bicycle.svg", "Transportation"),
            ImageItemAsset("rocket", "Space Rocket", "educational-images/transportation/rocket.svg", "Transportation"),
            ImageItemAsset("helicopter", "Helicopter", "educational-images/transportation/helicopter.svg", "Transportation")
        )
    )
}
