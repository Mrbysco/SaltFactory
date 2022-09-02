package com.mrbysco.saltfactory.data;

import com.google.gson.JsonObject;
import com.mrbysco.saltfactory.SaltFactory;
import com.mrbysco.saltfactory.registry.SaltRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SaltDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Recipes(generator));
			generator.addProvider(new GeoreItemTags(generator, new BlockTagsProvider(generator, SaltFactory.MOD_ID, helper), helper));
		}
		if (event.includeClient()) {
			generator.addProvider(new Language(generator));
			generator.addProvider(new SaltSoundProvider(generator, helper));
			generator.addProvider(new ItemModels(generator, helper));
		}
	}

	public static class Recipes extends RecipeProvider {

		public Recipes(DataGenerator generator) {
			super(generator);
		}

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(SaltRegistry.TEARY_BOWL.get()),
					SaltRegistry.DRIED_TEARS.get(), 0.01F, 200).unlockedBy("has_tears", has(SaltRegistry.TEARY_BOWL.get())).save(recipeConsumer);
			SimpleCookingRecipeBuilder.cooking(Ingredient.of(SaltRegistry.TEARY_BOWL.get()),
					SaltRegistry.DRIED_TEARS.get(), 0.01F, 400, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_tears",
					has(SaltRegistry.TEARY_BOWL.get())).save(recipeConsumer, "saltfactory:dried_tears_from_campfire");

			ShapelessRecipeBuilder.shapeless(SaltRegistry.CRYING_BOWL.get())
					.requires(Items.BOWL).requires(Tags.Items.RODS_WOODEN).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
					.save(recipeConsumer);
		}

		@Override
		protected void saveAdvancement(HashCache cache, JsonObject jsonObject, Path path) {
			//NOPE
		}
	}

	private static class Language extends LanguageProvider {
		public Language(DataGenerator gen) {
			super(gen, SaltFactory.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.saltfactory", "Salt Factory");

			addSubtitle(SaltRegistry.CRYING, "Pitiful Crying");

			addItem(SaltRegistry.CRYING_BOWL, "Crying Bowl");
			addItem(SaltRegistry.TEARY_BOWL, "Teary Bowl");
			addItem(SaltRegistry.DRIED_TEARS, "Dried tears");
		}

		public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
			String path = SaltFactory.MOD_ID + ".subtitle." + sound.getId().getPath();
			this.add(path, name);
		}
	}

	public static class SaltSoundProvider extends SoundDefinitionsProvider {
		public SaltSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
			super(generator, SaltFactory.MOD_ID, helper);
		}

		@Override
		public void registerSounds() {
			this.add(SaltRegistry.CRYING, definition()
					.subtitle(modSubtitle(SaltRegistry.CRYING.getId()))
					.with(sound(modLoc("crying/crying1")),
							sound(modLoc("crying/crying2")),
							sound(modLoc("crying/crying3")),
							sound(modLoc("crying/crying4"))
					));
		}

		public String modSubtitle(ResourceLocation id) {
			return SaltFactory.MOD_ID + ".subtitle." + id.getPath();
		}

		public ResourceLocation modLoc(String name) {
			return new ResourceLocation(SaltFactory.MOD_ID, name);
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, SaltFactory.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			makeCryingBowl(SaltRegistry.CRYING_BOWL.get());
			makeTearyBowl(SaltRegistry.TEARY_BOWL.get());
			makeSalt(SaltRegistry.DRIED_TEARS.get());
		}

		private void makeSalt(Item item) {
			String path = item.getRegistryName().getPath();
			getBuilder(path)
					.parent(new UncheckedModelFile(mcLoc("item/generated")))
					.texture("layer0", modLoc(ITEM_FOLDER + "/" + path));
		}

		private void makeTearyBowl(Item item) {
			String path = item.getRegistryName().getPath();
			getBuilder(path)
					.parent(new UncheckedModelFile(mcLoc("item/generated")))
					.texture("layer0", modLoc(ITEM_FOLDER + "/crying_bowl_4"));
		}

		private void makeCryingBowl(Item item) {
			String path = item.getRegistryName().getPath();

			for (int i = 0; i < 5; i++) {
				String bowlPath = i == 0 ? path + "_empty" : path + "_" + i;
				getBuilder(bowlPath)
						.parent(new UncheckedModelFile(mcLoc("item/generated")))
						.texture("layer0", modLoc(ITEM_FOLDER + "/" + bowlPath));
			}
		}
	}

	public static class GeoreItemTags extends ItemTagsProvider {
		public GeoreItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagsProvider, SaltFactory.MOD_ID, existingFileHelper);
		}

		public static final TagKey<Item> SALT = forgeTag("salt");

		@Override
		protected void addTags() {
			this.tag(SALT).add(SaltRegistry.DRIED_TEARS.get());
		}

		private static TagKey<Item> forgeTag(String name) {
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}
}
