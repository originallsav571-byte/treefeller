package uz.treefeller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Tree Feller (Daraxt kesuvchi).
 *
 * Boltani qo'lingizga olib daraxtning bitta tanasini kessangiz, shu tanaga
 * ulangan yuqoridagi barcha loglar birdan yiqiladi.
 *
 * - Cho'kkalab (Shift) kesilsa, oddiy tarzda faqat bitta blok sinadi.
 * - Har bir qo'shimcha log boltaning chidamliligini 1 ga kamaytiradi.
 *   Bolta sinib qolmasligi uchun oxirgi 1 ta chidamlilik qoldiriladi.
 * - Atrofida barg yo'q loglar (masalan, o'zingiz qurgan uy) kesilmaydi.
 */
public class TreeFellerMod implements ModInitializer {
	public static final String MOD_ID = "treefeller";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Bir martada kesiladigan loglarning eng ko'p soni. */
	private static final int MAX_LOGS = 256;

	/** Birinchi sindirilgan blokdan gorizontal bo'yicha eng uzoq masofa (bloklarda). */
	private static final int MAX_HORIZONTAL_RADIUS = 8;

	@Override
	public void onInitialize() {
		PlayerBlockBreakEvents.AFTER.register(TreeFellerMod::onBlockBroken);
		LOGGER.info("Tree Feller yuklandi: boltani ushlab daraxt kesing!");
	}

	private static void onBlockBroken(Level level, Player player, BlockPos origin, BlockState state, BlockEntity blockEntity) {
		// Faqat server tomonida ishlaymiz.
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		// Faqat log bloklari.
		if (!state.is(BlockTags.LOGS)) {
			return;
		}

		// Shift bosilgan bo'lsa, oddiy kesish.
		if (player.isShiftKeyDown()) {
			return;
		}

		// Qo'lda bolta bo'lishi kerak.
		ItemStack tool = player.getMainHandItem();
		if (!tool.is(ItemTags.AXES)) {
			return;
		}

		Block logType = state.getBlock();

		// Birinchi sindirilgan blokka ulangan, xuddi shu turdagi loglarni topamiz.
		List<BlockPos> logs = new ArrayList<>();
		Set<BlockPos> visited = new HashSet<>();
		ArrayDeque<BlockPos> queue = new ArrayDeque<>();
		boolean touchesLeaves = false;

		visited.add(origin);
		queue.add(origin);

		while (!queue.isEmpty() && logs.size() < MAX_LOGS) {
			BlockPos current = queue.poll();

			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					for (int dz = -1; dz <= 1; dz++) {
						if (dx == 0 && dy == 0 && dz == 0) {
							continue;
						}

						BlockPos next = current.offset(dx, dy, dz);

						// Pastga tushmaymiz: faqat sindirilgan blokdan yuqoridagilar yiqiladi.
						if (next.getY() < origin.getY()) {
							continue;
						}

						// Juda uzoqqa ketib qolmaslik uchun.
						if (Math.abs(next.getX() - origin.getX()) > MAX_HORIZONTAL_RADIUS
								|| Math.abs(next.getZ() - origin.getZ()) > MAX_HORIZONTAL_RADIUS) {
							continue;
						}

						if (!visited.add(next)) {
							continue;
						}

						BlockState nextState = serverLevel.getBlockState(next);

						if (nextState.is(BlockTags.LEAVES)) {
							touchesLeaves = true;
						} else if (nextState.is(logType)) {
							logs.add(next);
							queue.add(next);
						}
					}
				}
			}
		}

		// Barg topilmasa, bu daraxt emas (masalan, qurilgan uy): tegmaymiz.
		if (!touchesLeaves || logs.isEmpty()) {
			return;
		}

		boolean creative = player.isCreative();
		int felled = 0;

		for (BlockPos pos : logs) {
			if (felled >= MAX_LOGS) {
				break;
			}

			// Bolta sinib ketmasligi uchun oxirgi 1 ta chidamlilik qoladi.
			if (!creative && tool.isDamageableItem() && tool.getDamageValue() >= tool.getMaxDamage() - 1) {
				break;
			}

			serverLevel.destroyBlock(pos, !creative, player);

			if (!creative) {
				tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			}

			felled++;
		}
	}
}
