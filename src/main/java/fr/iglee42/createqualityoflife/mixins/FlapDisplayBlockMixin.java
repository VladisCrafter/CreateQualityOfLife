package fr.iglee42.createqualityoflife.mixins;

import com.simibubi.create.content.trains.display.FlapDisplayBlock;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.foundation.gui.ScreenOpener;
import fr.iglee42.createqualityoflife.CreateQOL;
import fr.iglee42.createqualityoflife.screens.DisplayBoardEditScreen;
import fr.iglee42.createqualityoflife.utils.Features;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FlapDisplayBlock.class,remap = false)
public class FlapDisplayBlockMixin {

    @Inject(method = "use",at = @At("HEAD"),cancellable = true)
    private void inject(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir){
        if (player.isCrouching()){
            if (world.getBlockEntity(pos) instanceof FlapDisplayBlockEntity be && CreateQOL.isActivate(Features.DISPLAY_BOARD_MODIFICATION)){
                if (!be.isController){
                    be = be.getController();
                }

                final FlapDisplayBlockEntity finalBe = be;
                double yCoord = ray.getLocation()
                        .add(Vec3.atLowerCornerOf(ray.getDirection()
                                        .getOpposite()
                                        .getNormal())
                                .scale(.125f)).y;

                int lineIndex = finalBe.getLineIndexAt(yCoord);

                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->createQualityOfLife$displayScreen(finalBe,player,lineIndex));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    @OnlyIn(value = Dist.CLIENT)
    private static void createQualityOfLife$displayScreen(FlapDisplayBlockEntity be, Player player, int lineIndex) {
        if (!(player instanceof LocalPlayer))
            return;

        ScreenOpener.open(new DisplayBoardEditScreen(be,lineIndex));
    }
}
