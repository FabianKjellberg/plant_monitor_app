package com.example.learning_android.repositories

import com.example.learning_android.R
import com.example.learning_android.domain.model.DeviceIcon

object IconResource {
  val groupedIcons = mapOf(
    "Living & Sleep" to listOf(
      DeviceIcon("armchair", "Armchair", R.drawable.ic_select_armchair),
      DeviceIcon("bed", "Bed", R.drawable.ic_select_bed),
      DeviceIcon("couch", "Couch", R.drawable.ic_select_couch),
      DeviceIcon("dresser", "Dresser", R.drawable.ic_select_dresser),
      DeviceIcon("lamp", "Lamp", R.drawable.ic_select_lamp),
      DeviceIcon("lamp_pendant", "Pendant Lamp", R.drawable.ic_select_lamp_pendant),
      DeviceIcon("television", "TV", R.drawable.ic_select_television),
      DeviceIcon("television_simple", "TV Simple", R.drawable.ic_select_television_simple),
      DeviceIcon("desk", "Desk", R.drawable.ic_select_desk),
      DeviceIcon("office_chair", "Office Chair", R.drawable.ic_select_office_chair)
    ),
    "Kitchen & Dining" to listOf(
      DeviceIcon("cooking_pot", "Cooking Pot", R.drawable.ic_select_cooking_pot),
      DeviceIcon("oven", "Oven", R.drawable.ic_select_oven),
      DeviceIcon("coffee", "Coffee", R.drawable.ic_select_coffee),
      DeviceIcon("bowl_food", "Bowl Food", R.drawable.ic_select_bowl_food),
      DeviceIcon("bowl_steam", "Hot Bowl", R.drawable.ic_select_bowl_steam),
      DeviceIcon("fork_knife", "Dining", R.drawable.ic_select_fork_knife),
      DeviceIcon("martini", "Martini", R.drawable.ic_select_martini),
      DeviceIcon("wine", "Wine", R.drawable.ic_select_wine),
      DeviceIcon("ice_cream", "Ice Cream", R.drawable.ic_select_ice_cream),
      DeviceIcon("orange", "Orange", R.drawable.ic_select_orange),
      DeviceIcon("orange_slice", "Orange Slice", R.drawable.ic_select_orange_slice),
      DeviceIcon("pizza", "Pizza", R.drawable.ic_select_pizza),
      DeviceIcon("pepper", "Pepper", R.drawable.ic_select_pepper),
      DeviceIcon("avocado", "Avocado", R.drawable.ic_select_avocado)
    ),
    "Bath & Utility" to listOf(
      DeviceIcon("bathtub", "Bathtub", R.drawable.ic_select_bathtub),
      DeviceIcon("shower", "Shower", R.drawable.ic_select_shower),
      DeviceIcon("toilet", "Toilet", R.drawable.ic_select_toilet),
      DeviceIcon("broom", "Broom", R.drawable.ic_select_broomic),
      DeviceIcon("spray_bottle", "Cleaning", R.drawable.ic_select_spray_bottle),
      DeviceIcon("washer", "Washer", R.drawable.ic_select_printer), // Reusing printer if needed or placeholder
      DeviceIcon("elevator", "Elevator", R.drawable.ic_select_elevator),
      DeviceIcon("steps", "Steps", R.drawable.ic_select_steps),
      DeviceIcon("door", "Door", R.drawable.ic_select_door)
    ),
    "Garden & Nature" to listOf(
      DeviceIcon("plant", "Plant", R.drawable.ic_select_plant),
      DeviceIcon("cactus", "Cactus", R.drawable.ic_select_cactus),
      DeviceIcon("flower", "Flower", R.drawable.ic_select_flower),
      DeviceIcon("flower_lotus", "Lotus", R.drawable.ic_select_flower_lotus),
      DeviceIcon("flower_tulip", "Tulip", R.drawable.ic_select_flower_tulip),
      DeviceIcon("tree", "Tree", R.drawable.ic_select_tree),
      DeviceIcon("tree_evergreen", "Evergreen", R.drawable.ic_select_tree_evergreen),
      DeviceIcon("tree_palm", "Palm", R.drawable.ic_select_tree_palm),
      DeviceIcon("clover", "Clover", R.drawable.ic_select_clover),
      DeviceIcon("sun", "Sun", R.drawable.ic_select_sun),
      DeviceIcon("rainbow", "Rainbow", R.drawable.ic_select_rainbow),
      DeviceIcon("drop", "Water", R.drawable.ic_select_drop)
    ),
    "Pets & Animals" to listOf(
      DeviceIcon("dog", "Dog", R.drawable.ic_select_dog),
      DeviceIcon("cat", "Cat", R.drawable.ic_select_cat),
      DeviceIcon("rabbit", "Rabbit", R.drawable.ic_select_rabbit),
      DeviceIcon("bird", "Bird", R.drawable.ic_select_bird),
      DeviceIcon("paw_print", "Paw Print", R.drawable.ic_select_paw_print),
      DeviceIcon("bone", "Bone", R.drawable.ic_select_bone),
      DeviceIcon("butterfly", "Butterfly", R.drawable.ic_select_butterfly),
      DeviceIcon("bug_beetle", "Beetle", R.drawable.ic_select_bug_beetle)
    ),
    "Hobbies & Tech" to listOf(
      DeviceIcon("game_controller", "Gaming", R.drawable.ic_select_game_controller),
      // Updated monitor reference
      DeviceIcon("monitor", "Monitor", R.drawable.ic_select_monitor),
      DeviceIcon("desktop_tower", "PC Tower", R.drawable.ic_select_desktop_tower),
      DeviceIcon("guitar", "Guitar", R.drawable.ic_select_guitar),
      DeviceIcon("paint_brush", "Art", R.drawable.ic_select_paint_brush),
      DeviceIcon("palette", "Palette", R.drawable.ic_select_palette),
      DeviceIcon("books", "Books", R.drawable.ic_select_books),
      DeviceIcon("radio", "Radio", R.drawable.ic_select_radio),
      DeviceIcon("film_reel", "Movies", R.drawable.ic_select_film_reel),
      DeviceIcon("barbell", "Gym", R.drawable.ic_select_barbell),
      DeviceIcon("yarn", "Crafts", R.drawable.ic_select_yarn),
      DeviceIcon("lego", "Lego", R.drawable.ic_select_lego)
    )
  )

  val availableIcons = groupedIcons.values.flatten()

  fun getIconById(id: String?): DeviceIcon {
    return availableIcons.find { it.id == id }
      ?: availableIcons.find { it.id == "plant" }
      ?: availableIcons[0]
  }
}