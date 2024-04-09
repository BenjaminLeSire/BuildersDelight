package com.tynoxs.buildersdelight.content.block.connected.model;

import com.tynoxs.buildersdelight.BuildersDelight;
import com.tynoxs.buildersdelight.content.block.connected.model.CTConnectedPaneBakedModel;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class CTPaneModelLoader implements IGeometryLoader<CTPaneModelLoader.CTModelGeometry> {

    public static final ResourceLocation GENERATOR_LOADER = new ResourceLocation(BuildersDelight.MODID, "connectedpaneloader");

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register("connectedpaneloader", new CTPaneModelLoader());
    }

    @Override
    public CTModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new CTModelGeometry();
    }

    public static class CTModelGeometry implements IUnbakedGeometry<CTModelGeometry> {
        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new CTConnectedPaneBakedModel(context, modelLocation);
        }
    }
}
