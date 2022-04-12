/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import io.netty.buffer.Unpooled;
import net.dries007.tfc.TestHelper;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredients;
import net.dries007.tfc.common.recipes.outputs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for various network round-trip encoding and decoding cycles
 */
public class NetworkTests extends TestHelper
{
    @Test
    public void testIngredient()
    {
        final Ingredient before = Ingredient.of(Items.ACACIA_BOAT);
        final Ingredient after = encodeAndDecode(before, Ingredient::toNetwork, Ingredient::fromNetwork);
        assertIngredientEquals(before, after);
    }

    @Test
    public void testBlockIngredient()
    {
        final BlockIngredient before = BlockIngredients.of(Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW);
        final BlockIngredient after = encodeAndDecode(before, BlockIngredient::toNetwork, BlockIngredients::fromNetwork);
        assertEquals(before.getValidBlocks(), after.getValidBlocks());
    }

    @Test
    public void testTagBlockIngredient()
    {
        final BlockIngredient before = BlockIngredients.of(BlockTags.CORAL_BLOCKS);
        final BlockIngredient after = encodeAndDecode(before, BlockIngredient::toNetwork, BlockIngredients::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testAddHeatModifier()
    {
        final ItemStackModifier before = new AddHeatModifier(1234);
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testAddRemoveTraitModifier()
    {
        final ItemStackModifier before = new AddRemoveTraitModifier(false, FoodTraits.BRINED);
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testCopyFoodModifier()
    {
        final ItemStackModifier before = CopyFoodModifier.INSTANCE;
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testCopyHeatModifier()
    {
        final ItemStackModifier before = CopyHeatModifier.INSTANCE;
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testCopyInputModifier()
    {
        final ItemStackModifier before = CopyInputModifier.INSTANCE;
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testEmptyBowlModifier()
    {
        final ItemStackModifier before = EmptyBowlModifier.INSTANCE;
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testResetFoodModifier()
    {
        final ItemStackModifier before = EmptyBowlModifier.INSTANCE;
        final ItemStackModifier after = encodeAndDecode(before, ItemStackModifier::toNetwork, ItemStackModifiers::fromNetwork);
        assertEquals(before, after);
    }

    @Test
    public void testItemStackProvider()
    {
        final ItemStackProvider before = ItemStackProvider.of(new ItemStack(Items.GREEN_BANNER, 3), EmptyBowlModifier.INSTANCE, CopyHeatModifier.INSTANCE, new AddHeatModifier(32));
        final ItemStackProvider after = encodeAndDecode(before, ItemStackProvider::toNetwork, ItemStackProvider::fromNetwork);

        assertEquals(before.stack().getItem(), after.stack().getItem());
        assertEquals(before.stack().getCount(), after.stack().getCount());
        assertArrayEquals(before.modifiers(), after.modifiers());
    }

    private <T> T encodeAndDecode(T t, BiConsumer<T, FriendlyByteBuf> encode, Function<FriendlyByteBuf, T> decode)
    {
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        encode.accept(t, buffer);
        final T result = decode.apply(buffer);
        assertEquals(buffer.readableBytes(), 0);
        return result;
    }
}