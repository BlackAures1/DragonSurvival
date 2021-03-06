package by.jackraidenph.dragonsurvival.nest;

import net.minecraft.util.math.BlockPos;

public class ToggleRegeneration {
    public BlockPos nestPos;
    public boolean state;

    public ToggleRegeneration() {
    }

    public ToggleRegeneration(BlockPos nestPos, boolean state) {
        this.nestPos = nestPos;
        this.state = state;
    }
}
