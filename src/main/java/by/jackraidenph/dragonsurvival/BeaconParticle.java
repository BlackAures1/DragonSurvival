package by.jackraidenph.dragonsurvival;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public class BeaconParticle extends SpriteTexturedParticle {
    private double fallSpeed;

    public BeaconParticle(ClientWorld p_i232447_1_, double p_i232447_2_, double p_i232447_4_, double p_i232447_6_) {
        super(p_i232447_1_, p_i232447_2_, p_i232447_4_, p_i232447_6_);
    }

    public BeaconParticle(ClientWorld p_i232448_1_, double x, double y, double z, double xd, double yd, double zd) {
        super(p_i232448_1_, x, y, z, xd, yd, zd);
        this.gravity = 0.5f;
        fallSpeed = 0.01;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
//            this.setSpriteFromAge(this.sprites);
            this.yd += this.fallSpeed;
            this.move(0, this.yd, 0);
            if (this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }
            this.yd *= 0.96F;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }

        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
