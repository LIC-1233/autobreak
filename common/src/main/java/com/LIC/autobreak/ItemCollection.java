package com.LIC.autobreak;

import com.LIC.autobreak.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ItemCollection {
	private final List<ItemStack> items = new ArrayList<>();
	public static final Logger LOGGER = LogManager.getLogger();

	public List<ItemStack> getItems() {
		return items;
	}

	public void add(ItemStack is) {
		if (!is.isEmpty()) {
			items.add(is.copy());
		}
	}

	public void drop(Level world, BlockPos pos) {
		if (items.isEmpty()) {
			return;
		}

		List<ItemStack> stacks = new ArrayList<>();

		for (ItemStack stack : items) {
			LOGGER.info("Handling item {}", stack.getDisplayName().getString());
			if (!stack.isStackable()) {
				LOGGER.info("Item is not stackable, dropping it");
				stacks.add(stack);
				continue;
			}

			int sizeInventory = stacks.size();

			// go through the inventory and try to fill up already existing items
			for (int i = 0; i < sizeInventory; i++) {

				LOGGER.info("stacking item {}", stacks.get(i).getDisplayName().getString());

				stack = insert(stacks, stack, i);

				if (stack.isEmpty()) {
					break;
				}
			}

			if (!stack.isEmpty()) {
				stacks.add(stack);
			}

			//Block.spawnAsEntity(world, pos, ItemHandlerHelper.insertItemStacked(handler, stack, false));
		}

		for (ItemStack stack : stacks) {
			LOGGER.info("Dropping item {}", stack.getDisplayName().getString());
			Block.popResource(world, pos, stack);
		}
	}

	private ItemStack insert(List<ItemStack> stacks, ItemStack stack, int slot) {
		ItemStack existing = stacks.get(slot);

		if (stack.isEmpty() || existing.isEmpty() || stack.getItem() != existing.getItem()) {
			return stack;
		}

		// intellij doesn't like that we throw something here
		//noinspection ConstantConditions
		if (!stack.isStackable() || !Objects.equals(stack.getTag(), existing.getTag()) || !ItemUtils.areCompatible(stack, existing)) {
			return stack;
		}

		int limit = stack.getMaxStackSize();

		if (!existing.isEmpty()) {
			if (!ItemUtils.canItemStacksStack(stack, existing)) {
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0) {
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (existing.isEmpty()) {
			stacks.set(slot, reachedLimit ? ItemUtils.copyStackWithSize(stack, limit) : stack);
		} else {
			existing.grow(reachedLimit ? limit : stack.getCount());
		}

		return reachedLimit ? ItemUtils.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}
}