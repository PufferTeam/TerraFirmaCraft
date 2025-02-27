/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.blocks.rock;

import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCMaterials;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.util.registry.RegistryRock;

/**
 * Default rocks that are used for block registration calls. Not extensible.
 */
public enum Rock implements RegistryRock
{
    GRANITE(RockCategory.IGNEOUS_INTRUSIVE, MaterialColor.STONE, SandBlockType.BROWN),
    DIORITE(RockCategory.IGNEOUS_INTRUSIVE, MaterialColor.METAL, SandBlockType.WHITE),
    GABBRO(RockCategory.IGNEOUS_INTRUSIVE, MaterialColor.COLOR_GRAY, SandBlockType.BLACK),
    SHALE(RockCategory.SEDIMENTARY, MaterialColor.COLOR_GRAY, SandBlockType.BLACK),
    CLAYSTONE(RockCategory.SEDIMENTARY, MaterialColor.TERRACOTTA_YELLOW, SandBlockType.BROWN),
    LIMESTONE(RockCategory.SEDIMENTARY, MaterialColor.TERRACOTTA_WHITE, SandBlockType.WHITE),
    CONGLOMERATE(RockCategory.SEDIMENTARY, MaterialColor.TERRACOTTA_LIGHT_GRAY, SandBlockType.GREEN),
    DOLOMITE(RockCategory.SEDIMENTARY, MaterialColor.COLOR_GRAY, SandBlockType.BLACK),
    CHERT(RockCategory.SEDIMENTARY, MaterialColor.TERRACOTTA_ORANGE, SandBlockType.YELLOW),
    CHALK(RockCategory.SEDIMENTARY, MaterialColor.QUARTZ, SandBlockType.WHITE),
    RHYOLITE(RockCategory.IGNEOUS_EXTRUSIVE, MaterialColor.TERRACOTTA_LIGHT_GRAY, SandBlockType.RED),
    BASALT(RockCategory.IGNEOUS_EXTRUSIVE, MaterialColor.COLOR_BLACK, SandBlockType.RED),
    ANDESITE(RockCategory.IGNEOUS_EXTRUSIVE, MaterialColor.TERRACOTTA_CYAN, SandBlockType.RED),
    DACITE(RockCategory.IGNEOUS_EXTRUSIVE, MaterialColor.STONE, SandBlockType.RED),
    QUARTZITE(RockCategory.METAMORPHIC, MaterialColor.TERRACOTTA_WHITE, SandBlockType.YELLOW),
    SLATE(RockCategory.METAMORPHIC, MaterialColor.WOOD, SandBlockType.BROWN),
    PHYLLITE(RockCategory.METAMORPHIC, MaterialColor.TERRACOTTA_LIGHT_BLUE, SandBlockType.BROWN),
    SCHIST(RockCategory.METAMORPHIC, MaterialColor.TERRACOTTA_LIGHT_GREEN, SandBlockType.GREEN),
    GNEISS(RockCategory.METAMORPHIC, MaterialColor.TERRACOTTA_LIGHT_GRAY, SandBlockType.GREEN),
    MARBLE(RockCategory.METAMORPHIC, MaterialColor.WOOL, SandBlockType.WHITE);

    public static final Rock[] VALUES = values();

    private final String serializedName;
    private final RockCategory category;
    private final MaterialColor color;
    private final SandBlockType sandType;

    Rock(RockCategory category, MaterialColor color, SandBlockType sandType)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
        this.sandType = sandType;
    }

    public SandBlockType getSandType()
    {
        return sandType;
    }

    @Override
    public RockCategory category()
    {
        return category;
    }

    @Override
    public MaterialColor color()
    {
        return color;
    }

    @Override
    public Supplier<? extends Block> getBlock(BlockType type)
    {
        return TFCBlocks.ROCK_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<? extends Block> getAnvil()
    {
        return TFCBlocks.ROCK_ANVILS.get(this);
    }

    @Override
    public Supplier<? extends SlabBlock> getSlab(BlockType type)
    {
        return TFCBlocks.ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> getStair(BlockType type)
    {
        return TFCBlocks.ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> getWall(BlockType type)
    {
        return TFCBlocks.ROCK_DECORATIONS.get(this).get(type).wall();
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public enum BlockType implements StringRepresentable
    {
        RAW((rock, self) -> RockConvertableToAnvilBlock.createForIgneousOnly(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops(), rock, false), true),
        HARDENED((rock, self) -> RockConvertableToAnvilBlock.createForIgneousOnly(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(8f), 10).requiresCorrectToolForDrops(), rock, true), false),
        SMOOTH((rock, self) -> new Block(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        COBBLE((rock, self) -> new MossGrowingBlock(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(5.5f), 10).requiresCorrectToolForDrops(), rock.getBlock(Objects.requireNonNull(self.mossy()))), true),
        BRICKS((rock, self) -> new MossGrowingBlock(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops(), rock.getBlock(Objects.requireNonNull(self.mossy()))), true),
        GRAVEL((rock, self) -> new Block(Block.Properties.of(Material.SAND, rock.color()).sound(SoundType.GRAVEL).strength(rock.category().hardness(2.0f))), false),
        SPIKE((rock, self) -> new RockSpikeBlock(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(4f), 10).requiresCorrectToolForDrops().lightLevel(TFCBlocks.lavaLoggedBlockEmission())), false),
        CRACKED_BRICKS((rock, self) -> new Block(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        MOSSY_BRICKS((rock, self) -> new MossSpreadingBlock(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        MOSSY_COBBLE((rock, self) -> new MossSpreadingBlock(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        CHISELED((rock, self) -> new Block(Block.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(8f), 10).requiresCorrectToolForDrops()), false),
        LOOSE((rock, self) -> new LooseRockBlock(Block.Properties.of(TFCMaterials.NON_SOLID_STONE, rock.color()).strength(0.05f, 0.0f).sound(SoundType.STONE).noCollission()), false),
        PRESSURE_PLATE((rock, self) -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of(Material.STONE, rock.color()).requiresCorrectToolForDrops().noCollission().strength(0.5f)), false),
        BUTTON((rock, self) -> new StoneButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION, rock.color()).noCollission().strength(0.5f)), false),
        AQUEDUCT((rock, self) -> new AqueductBlock(BlockBehaviour.Properties.of(Material.STONE, rock.color()).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false);

        public static final BlockType[] VALUES = BlockType.values();

        public static BlockType valueOf(int i)
        {
            return i >= 0 && i < VALUES.length ? VALUES[i] : RAW;
        }

        private final boolean variants;
        private final BiFunction<RegistryRock, BlockType, Block> blockFactory;
        private final String serializedName;

        BlockType(BiFunction<RegistryRock, BlockType, Block> blockFactory, boolean variants)
        {
            this.blockFactory = blockFactory;
            this.variants = variants;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        /**
         * @return if this block type should be given slab, stair and wall variants
         */
        public boolean hasVariants()
        {
            return variants;
        }

        public Block create(RegistryRock rock)
        {
            return blockFactory.apply(rock, this);
        }

        public SlabBlock createSlab(RegistryRock rock)
        {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final BlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingSlabBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingSlabBlock(properties, rock.getSlab(mossy));
            }
            return new SlabBlock(properties);
        }

        public StairBlock createStairs(RegistryRock rock)
        {
            final Supplier<BlockState> state = () -> rock.getBlock(this).get().defaultBlockState();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final BlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingStairBlock(state, properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingStairsBlock(state, properties, rock.getStair(mossy));
            }
            return new StairBlock(state, properties);
        }

        public WallBlock createWall(RegistryRock rock)
        {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final BlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingWallBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingWallBlock(properties, rock.getWall(mossy));
            }
            return new WallBlock(properties);
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }

        @Nullable
        private BlockType mossy()
        {
            return switch (this)
                {
                    case COBBLE, MOSSY_COBBLE -> MOSSY_COBBLE;
                    case BRICKS, MOSSY_BRICKS -> MOSSY_BRICKS;
                    default -> null;
                };
        }
    }
}
