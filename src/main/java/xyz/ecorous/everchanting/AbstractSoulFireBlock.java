package xyz.ecorous.everchanting;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.AreaHelper;

import java.util.Optional;

public class AbstractSoulFireBlock extends AbstractFireBlock {
	public AbstractSoulFireBlock(Settings settings, float floatd, float damage) {
		super(settings, floatd);
		this.damage = damage;
	}

	@Override
	protected boolean isFlammable(BlockState state) {
		return false;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getState(ctx.getWorld(), ctx.getBlockPos());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {return BASE_SHAPE;}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (random.nextInt(24) == 0) {
			world.playSound(
					(double)pos.getX() + 0.5,
					(double)pos.getY() + 0.5,
					(double)pos.getZ() + 0.5,
					SoundEvents.BLOCK_FIRE_AMBIENT,
					SoundCategory.BLOCKS,
					1.0F + random.nextFloat(),
					random.nextFloat() * 0.7F + 0.3F,
					false
			);
		}

		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		if (!this.isFlammable(blockState) && !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
			if (this.isFlammable(world.getBlockState(pos.west()))) {
				for(int i = 0; i < 2; ++i) {
					double d = (double)pos.getX() + random.nextDouble() * 0.1F;
					double e = (double)pos.getY() + random.nextDouble();
					double f = (double)pos.getZ() + random.nextDouble();
					world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}

			if (this.isFlammable(world.getBlockState(pos.east()))) {
				for(int i = 0; i < 2; ++i) {
					double d = (double)(pos.getX() + 1) - random.nextDouble() * 0.1F;
					double e = (double)pos.getY() + random.nextDouble();
					double f = (double)pos.getZ() + random.nextDouble();
					world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}

			if (this.isFlammable(world.getBlockState(pos.north()))) {
				for(int i = 0; i < 2; ++i) {
					double d = (double)pos.getX() + random.nextDouble();
					double e = (double)pos.getY() + random.nextDouble();
					double f = (double)pos.getZ() + random.nextDouble() * 0.1F;
					world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}

			if (this.isFlammable(world.getBlockState(pos.south()))) {
				for(int i = 0; i < 2; ++i) {
					double d = (double)pos.getX() + random.nextDouble();
					double e = (double)pos.getY() + random.nextDouble();
					double f = (double)(pos.getZ() + 1) - random.nextDouble() * 0.1F;
					world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}

			if (this.isFlammable(world.getBlockState(pos.up()))) {
				for(int i = 0; i < 2; ++i) {
					double d = (double)pos.getX() + random.nextDouble();
					double e = (double)(pos.getY() + 1) - random.nextDouble() * 0.1F;
					double f = (double)pos.getZ() + random.nextDouble();
					world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
		} else {
			for(int i = 0; i < 3; ++i) {
				double d = (double)pos.getX() + random.nextDouble();
				double e = (double)pos.getY() + random.nextDouble() * 0.5 + 0.5;
				double f = (double)pos.getZ() + random.nextDouble();
				world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
			}
		}

	}
	private final float damage;
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!entity.isFireImmune()) {
			entity.setFireTicks(entity.getFireTicks() + 1);
			if (entity.getFireTicks() == 0) {
				entity.setOnFireFor(8);
			}
		}

		entity.damage(DamageSource.IN_FIRE, this.damage);
		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock())) {
			if (isOverworldOrNether(world)) {
				Optional<AreaHelper> optional = AreaHelper.getNewPortal(world, pos, Direction.Axis.X);
				if (optional.isPresent()) {
					((AreaHelper)optional.get()).createPortal();
					return;
				}
			}

			if (!state.canPlaceAt(world, pos)) {
				world.removeBlock(pos, false);
			}

		}
	}
	private static boolean isOverworldOrNether(World world) {
		return world.getRegistryKey() == World.OVERWORLD || world.getRegistryKey() == World.NETHER;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient()) {
			world.syncWorldEvent(null, 1009, pos, 0);
		}

		super.onBreak(world, pos, state, player);
	}


	public static BlockState getState(BlockView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return Blocks.SOUL_FIRE.getDefaultState();
	}

	public static boolean canPlaceAt(World world, BlockPos pos, Direction direction) {
		BlockState blockState = world.getBlockState(pos);
		if (!blockState.isAir()) {
			return false;
		} else {
			return getState(world, pos).canPlaceAt(world, pos) || shouldLightPortalAt(world, pos, direction);
		}
	}
	private static boolean shouldLightPortalAt(World world, BlockPos pos, Direction direction) {
		if (!isOverworldOrNether(world)) {
			return false;
		} else {
			BlockPos.Mutable mutable = pos.mutableCopy();
			boolean bl = false;

			for(Direction direction2 : Direction.values()) {
				if (world.getBlockState(mutable.set(pos).move(direction2)).isOf(Blocks.OBSIDIAN)) {
					bl = true;
					break;
				}
			}

			if (!bl) {
				return false;
			} else {
				Direction.Axis axis = direction.getAxis().isHorizontal()
						? direction.rotateYCounterclockwise().getAxis()
						: Direction.Type.HORIZONTAL.randomAxis(world.random);
				return AreaHelper.getNewPortal(world, pos, axis).isPresent();
			}
		}
	}
}
