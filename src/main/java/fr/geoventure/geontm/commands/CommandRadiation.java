package fr.geoventure.geontm.commands;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.handler.radiation.ChunkRadiationManager;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.geoventure.geocore.command.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * 1.7 {@code CommandRadiation}, {@code /ntmrad <set <amount>|clear>}: sets the chunk radiation at the sender's block
 * (0 to 100,000 RAD, 1.7's {@code parseDoubleBounded}) or wipes the level's whole radiation system. Anything else
 * prints the usage in red, as 1.7 did. The port's finer-grained debug tree is {@code /geontm rad}.
 */
public final class CommandRadiation {

    private CommandRadiation() {}

    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("ntmrad").requires(Permissions.level(4))
                .executes(ctx -> usage(ctx.getSource()))
                .then(Commands.literal("clear").executes(ctx -> {
                    ChunkRadiationManager.proxy.clearSystem(ctx.getSource().getLevel());
                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.geontm.ntmrad.cleared"), true);
                    return 1;
                }))
                // Geoventure : fixe la dose d'un joueur pour mesurer la radioactivité sur le serveur de test.
                .then(Commands.literal("joueur")
                        .then(Commands.argument("cible", EntityArgument.player())
                                .then(Commands.argument("dose", DoubleArgumentType.doubleArg(0D, 2_500D)).executes(ctx -> {
                                    ServerPlayer cible = EntityArgument.getPlayer(ctx, "cible");
                                    double dose = DoubleArgumentType.getDouble(ctx, "dose");
                                    GeoNtmLivingProps.setRadiation(cible, dose);
                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                            "Dose de " + cible.getScoreboardName() + " : " + dose + " RAD"), true);
                                    return 1;
                                }))))
                .then(Commands.literal("set")
                        .executes(ctx -> usage(ctx.getSource()))
                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0D, 100_000D)).executes(ctx -> {
                            // 1.7 parsed a double and stored it as a float
                            float amount = (float) DoubleArgumentType.getDouble(ctx, "amount");
                            BlockPos pos = BlockPos.containing(ctx.getSource().getPosition());
                            ChunkRadiationManager.proxy.setRadiation(ctx.getSource().getLevel(), pos, amount);
                            ctx.getSource().sendSuccess(() -> Component.translatable("commands.geontm.ntmrad.set"), true);
                            return 1;
                        })));
    }

    private static int usage(CommandSourceStack source) {
        source.sendSystemMessage(Component.translatable("commands.geontm.ntmrad.usage").withStyle(ChatFormatting.RED));
        return 0;
    }
}
