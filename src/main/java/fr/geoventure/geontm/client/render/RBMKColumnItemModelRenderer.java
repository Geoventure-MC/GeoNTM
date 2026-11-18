package fr.geoventure.geontm.client.render;

//? if >=26.1 {

/*import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.geoventure.geocore.render.VanillaSubmitSink;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3fc;
import java.util.function.Consumer;

/^* 26.1 codec for a complete four-segment RBMK fuel-channel item, not a hollow block OBJ. ^/
public final class RBMKColumnItemModelRenderer implements NoDataSpecialModelRenderer {
    public static final ResourceLocation ID = fr.geoventure.geocore.Ids.of("geontm", "rbmk_fuel_column");
    private final RBMKColumnItemModel model;
    private RBMKColumnItemModelRenderer(RBMKColumnItemModel model) { this.model = model; }
    @Override public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) { model.submit(pose, new VanillaSubmitSink(collector), light, overlay); }
    @Override public void getExtents(Consumer<Vector3fc> output) { model.extents(output); }
    public record Unbaked(ResourceLocation side, ResourceLocation top, ResourceLocation inner, ResourceLocation fuel, float scale, float yaw, float lift) implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                ResourceLocation.CODEC.fieldOf("side").forGetter(Unbaked::side), ResourceLocation.CODEC.fieldOf("top").forGetter(Unbaked::top),
                ResourceLocation.CODEC.fieldOf("inner").forGetter(Unbaked::inner), ResourceLocation.CODEC.fieldOf("fuel").forGetter(Unbaked::fuel), Codec.FLOAT.optionalFieldOf("scale", .35F).forGetter(Unbaked::scale),
                Codec.FLOAT.optionalFieldOf("yaw", 0F).forGetter(Unbaked::yaw), Codec.FLOAT.optionalFieldOf("lift", -.675F).forGetter(Unbaked::lift)
        ).apply(i, Unbaked::new));
        @Override public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() { return MAP_CODEC; }
        @Override public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) { return new RBMKColumnItemModelRenderer(new RBMKColumnItemModel(side, top, inner, fuel, scale, yaw, lift)); }
    }
}
*///?}
