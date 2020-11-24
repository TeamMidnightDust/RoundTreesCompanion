package eu.midnightdust.roundtrees.mixin;

import eu.midnightdust.roundtrees.RoundTreesClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SignBlockEntityRenderer.class)
public abstract class MixinSignBlockEntityRenderer extends BlockEntityRenderer<SignBlockEntity> {


    public MixinSignBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Shadow @Final private SignBlockEntityRenderer.SignModel model;
    @Shadow public static SpriteIdentifier getModelTexture(Block block) {
        return null;
    }


    @Inject(at = @At("TAIL"), method = "render")
    @Environment(EnvType.CLIENT)
    public void render(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        BlockState blockState = signBlockEntity.getCachedState();
        float h = 0;

        if (blockState.getBlock() instanceof WallSignBlock && RoundTreesClient.RT_CONFIG.sign_fix) {
            switch (blockState.get(WallSignBlock.FACING)) {
                case NORTH:if (signBlockEntity.getWorld().getBlockState(signBlockEntity.getPos().south()).getBlock().toString().toLowerCase().contains("log")) {renderSignExtention(signBlockEntity, f, matrixStack, vertexConsumerProvider, i, j, h, blockState);};
                case EAST:if (signBlockEntity.getWorld().getBlockState(signBlockEntity.getPos().west()).getBlock().toString().toLowerCase().contains("log")) {renderSignExtention(signBlockEntity, f, matrixStack, vertexConsumerProvider, i, j, h, blockState);};
                case SOUTH:if (signBlockEntity.getWorld().getBlockState(signBlockEntity.getPos().north()).getBlock().toString().toLowerCase().contains("log")) {renderSignExtention(signBlockEntity, f, matrixStack, vertexConsumerProvider, i, j, h, blockState);};
                case WEST:if (signBlockEntity.getWorld().getBlockState(signBlockEntity.getPos().east()).getBlock().toString().toLowerCase().contains("log")) {renderSignExtention(signBlockEntity, f, matrixStack, vertexConsumerProvider, i, j, h, blockState);};
                default:;
            }
        }
    }

    public void renderSignExtention(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, float h, BlockState blockState) {
        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        h = -((Direction) blockState.get(WallSignBlock.FACING)).asRotation();
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(h));
        matrixStack.translate(0.0D, -0.3125D, -0.4375D+0.04);
        matrixStack.push();
        matrixStack.scale(0.6667F, -0.6667F, -0.6667F);
        SpriteIdentifier spriteIdentifier = getModelTexture(blockState.getBlock());
        SignBlockEntityRenderer.SignModel var10002 = this.model;
        var10002.getClass();
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, var10002::getLayer);
        this.model.field.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
        TextRenderer textRenderer = this.dispatcher.getTextRenderer();
        matrixStack.translate(0.0D, 0.3333333432674408D, 0.046666666865348816D+0.013);
        matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int m = signBlockEntity.getTextColor().getSignColor();
        int n = (int)((double) NativeImage.getRed(m) * 0.4D);
        int o = (int)((double)NativeImage.getGreen(m) * 0.4D);
        int p = (int)((double)NativeImage.getBlue(m) * 0.4D);
        int q = NativeImage.getAbgrColor(0, p, o, n);

        for(int s = 0; s < 4; ++s) {
            OrderedText orderedText = signBlockEntity.getTextBeingEditedOnRow(s, (text) -> {
                List<OrderedText> list = textRenderer.wrapLines(text, 90);
                return list.isEmpty() ? OrderedText.EMPTY : (OrderedText)list.get(0);
            });
            if (orderedText != null) {
                float t = (float)(-textRenderer.getWidth(orderedText) / 2);
                textRenderer.draw(orderedText, t, (float)(s * 10 - 20), q, false, matrixStack.peek().getModel(), vertexConsumerProvider, false, 0, i);
            }
        }
        matrixStack.pop();
    }
}
