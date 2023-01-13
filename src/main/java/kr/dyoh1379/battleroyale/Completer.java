package kr.dyoh1379.battleroyale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Completer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> list = Arrays.asList("help", "start", "stop", "set", "info");

        List<String> list2 = Arrays.asList("GameProcess", "MaxBorderSize", "BorderTime", "MaxPlayers");
        List<String> setinfo_list = Arrays.asList("set", "info");
        List<String> boolean_list = Arrays.asList("true", "false");

        List<String> completions = null;

        switch (args.length) {

            case 1:
                for (String s : list) {
                    if (s.startsWith(args[0].toLowerCase())) {
                        if (completions == null) {
                            completions = new ArrayList<>();
                        }

                        completions.add(s);
                    }
                }
                break;

            case 2:
                if (setinfo_list.contains(args[0].toLowerCase())) {

                    for (String s : list2) {
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                            if (completions == null) {
                                completions = new ArrayList<>();
                            }

                            completions.add(s);
                        }
                    }
                }
                break;

            case 3:
                if ("set".equalsIgnoreCase(args[0]) && "GameProcess".equalsIgnoreCase(args[1])) {

                    for (String s : boolean_list) {
                        if (s.startsWith(args[2].toLowerCase())) {
                            if (completions == null) {
                                completions = new ArrayList<>();
                            }

                            completions.add(s);
                        }
                    }
                }
                break;

        }

        return completions;
    }
}
