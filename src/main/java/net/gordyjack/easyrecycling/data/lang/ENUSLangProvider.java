package net.gordyjack.easyrecycling.data.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.gordyjack.easyrecycling.block.ModBlocks;
import net.gordyjack.easyrecycling.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import org.apache.commons.lang3.text.WordUtils;

import java.nio.file.Path;

public class ENUSLangProvider extends FabricLanguageProvider {
    public ENUSLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }
    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        for (Block block : ModBlocks.BLOCKS) {
            translationBuilder.add(block, getTranslatedName(block));
        }
        for (Item item : ModItems.ITEMS) {
            translationBuilder.add(item, getTranslatedName(item));
        }

        try {
            Path existingFilePath = dataOutput.getModContainer().findPath("assets/easyrecycling/lang/en_us.existing.json").get();
            translationBuilder.add(existingFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }
    private String getTranslatedName(ItemConvertible itemConvertible) {
        String returnString = itemConvertible.asItem().getTranslationKey().toString();
        returnString = returnString.substring(returnString.lastIndexOf('.') + 1);
        returnString = returnString.replace('_', ' ');
        returnString = WordUtils.capitalizeFully(returnString);
        return returnString;
    }
}
