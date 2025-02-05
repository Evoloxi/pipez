package de.maxhenkel.pipez;

import de.maxhenkel.corelib.CachedValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public class ModelRegistry {

    public enum Model {
        ENERGY_PIPE_EXTRACT("block/energy_pipe_extract"),
        FLUID_PIPE_EXTRACT("block/fluid_pipe_extract"),
        GAS_PIPE_EXTRACT("block/gas_pipe_extract"),
        ITEM_PIPE_EXTRACT("block/item_pipe_extract"),
        UNIVERSAL_PIPE_EXTRACT("block/universal_pipe_extract");

        private final ResourceLocation resource;
        private final CachedValue<BakedModel> cachedModel;

        Model(String name) {
            resource = new ResourceLocation(Main.MODID, name);
            cachedModel = new CachedValue<>(() -> {
                ModelBakery modelBakery = Minecraft.getInstance().getModelManager().getModelBakery();
                UnbakedModel modelOrMissing = modelBakery.getModel(resource);
                return modelOrMissing.bake(modelBakery, modelBakery.getAtlasSet()::getSprite, BlockModelRotation.X0_Y0, resource);
            });
        }

        public ResourceLocation getResourceLocation() {
            return resource;
        }

        public CachedValue<BakedModel> getCachedModel() {
            return cachedModel;
        }
    }

    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        for (Model model : Model.values()) {
            event.register(model.getResourceLocation());
//            ModelBakery.instance().addSpecialModel(model.getResourceLocation());
        }
    }

    public static void onModelBake(ModelEvent.BakingCompleted event) {
        for (Model model : Model.values()) {
            model.getCachedModel().invalidate();
        }
    }

}
