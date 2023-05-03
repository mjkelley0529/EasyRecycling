package net.gordyjack.easyrecycling.data.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.block.RecyclingTableBlock;
import net.minecraft.item.ItemConvertible;
import org.apache.commons.lang3.text.WordUtils;

import java.nio.file.Path;

public class ENUSLangProvider extends FabricLanguageProvider {
    public ENUSLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }
    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(EasyRecycling.RECYCLING_TABLE_BLOCK, getTranslatedName(EasyRecycling.RECYCLING_TABLE_BLOCK));
        translationBuilder.add(RecyclingTableBlock.TITLE_KEY, getTranslatedName(RecyclingTableBlock.TITLE_KEY));

        try {
            Path existingFilePath = dataOutput.getModContainer().findPath("assets/easyrecycling/lang/en_us.existing.json").get();
            translationBuilder.add(existingFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }
    private String getTranslatedName(ItemConvertible itemConvertible) {
        return getTranslatedName(itemConvertible.asItem().getTranslationKey());
    }
    private String getTranslatedName(String name) {
        name = name.substring(name.lastIndexOf('.') + 1);
        name = name.replace('_', ' ');
        name = WordUtils.capitalizeFully(name);
        return name;
    }
}
