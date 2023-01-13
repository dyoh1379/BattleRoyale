package kr.dyoh1379.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static kr.dyoh1379.battleroyale.Function.*;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        switch (command.getName()) {

            case "battle":

                switch (args.length) {

                    case 1:

                        switch (args[0].toLowerCase()) {

                            case "help":
                                sender.sendMessage("도움말");
                                break;

                            case "start":
                                if (config.getBoolean("GameProcess")) {
                                    sender.sendMessage("이미 게임이 시작되었습니다");
                                    return false;
                                }

                                config.set("GameProcess", true);
                                plugin.saveConfig();
                                startGame();
                                sender.sendMessage("게임 시작!");
                                break;

                            case "stop":
                                if (!config.getBoolean("GameProcess")) {
                                    sender.sendMessage("이미 게임이 종료되었습니다");
                                    return false;
                                }

                                config.set("GameProcess", false);
                                plugin.saveConfig();
                                sender.sendMessage("게임 종료");
                                break;
                        }

                    case 2:

                        switch (args[0].toLowerCase()) {

                            case "info":

                                switch (args[1].toLowerCase()) {

                                    case "maxbordersize":
                                        sender.sendMessage("MaxBorderSize: " + config.getDouble("MaxBorderSize"));
                                        break;

                                    case "bordertime":
                                        sender.sendMessage("BorderTime: " + config.getInt("BorderTime"));
                                        break;

                                    case "gameprocess":
                                        sender.sendMessage("GameProcess: " + config.getBoolean("GameProcess"));
                                        break;

                                    case "maxplayers":
                                        sender.sendMessage("MaxPlayers: " + Bukkit.getMaxPlayers());
                                        break;

                                }

                        }

                    case 3:

                        switch (args[0].toLowerCase()) {

                            case "set":

                                switch (args[1].toLowerCase()) {

                                    case "maxbordersize":
                                        if (!isDouble(args[2])) {
                                            sender.sendMessage("실수 만 입력 가능합니다");
                                            return false;
                                        }

                                        config.set("MaxBorderSize",  Double.parseDouble(args[2]));
                                        plugin.saveConfig();
                                        sender.sendMessage("MaxBorderSize 값이 " + Double.parseDouble(args[2]) + "(으)로 변경되었습니다");
                                        break;

                                    case "bordertime":
                                        if (!isInteger(args[2])) {
                                            sender.sendMessage("정수 만 입력 가능합니다");
                                            return false;
                                        }

                                        int valueSource = Integer.parseInt(args[2]);
                                        String value;

                                        if (valueSource % 60 > 0) {
                                            value = valueSource / 60 + "분 " + valueSource % 60 + "초";
                                        } else {
                                            value = valueSource / 60 + "분 ";
                                        }

                                        config.set("BorderTime", valueSource);
                                        plugin.saveConfig();
                                        sender.sendMessage("BorderTime 값이 " + value + "(으)로 변경되었습니다");
                                        break;

                                    case "gameprocess":
                                        if (!isBoolean(args[2])) {
                                            sender.sendMessage("boolean 만 입력 가능합니다");
                                            return false;
                                        }

                                        config.set("GameProcess", Boolean.parseBoolean(args[2].toLowerCase()));
                                        plugin.saveConfig();
                                        sender.sendMessage("GameProcess 값이 " + args[2].toLowerCase() + "(으)로 변경되었습니다");
                                        break;

                                    case "maxplayers":
                                        if (!isInteger(args[2])) {
                                            sender.sendMessage("정수 만 입력 가능합니다");
                                            return false;
                                        }

                                        Bukkit.setMaxPlayers(Integer.parseInt(args[2]));
                                        sender.sendMessage("MaxPlayers 값이 " + args[2] + "(으)로 변경되었습니다");
                                        break;

                                }

                        }

                }
        }


        return false;
    }

}
