package net.gordyjack.easyrecycling.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RecyclingScreen extends HandledScreen<RecyclingScreenHandler> {
    //Constructor
    public RecyclingScreen(RecyclingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    //Fields
    public static final Identifier TEXTURE = EasyRecycling.getID("textures/gui/containers/recycling_table.png");

    //Methods
    //Custom Methods
    private int xD2() {
        return (width-backgroundWidth)/2;
    }
    private int yD2() {
        return (height-backgroundHeight)/2;
    }
    //Inherited Methods
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        drawTexture(matrices, xD2(), yD2(), 0, 0, backgroundWidth, backgroundHeight);
        if (this.handler.getSlot(0).hasStack() && !this.handler.getSlot(1).hasStack()) {
            drawTexture(matrices, xD2() + 92, yD2() + 31, this.backgroundWidth, 0, 28, 21);
        }

        renderFuel(matrices, xD2(), yD2());
        renderProgressArrow(matrices, xD2(), yD2());
    }
    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth-textRenderer.getWidth(title))/2;
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    //TODO: Change these methods and texture file to appropriately display a progress arrow and grinding material indicator
    private void renderFuel(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 105, y + 33, 176, 0, 8, handler.getScaledFuel());
        }
    }
    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 105, y + 33, 176, 0, 8, handler.getScaledProgress());
        }
    }
}
