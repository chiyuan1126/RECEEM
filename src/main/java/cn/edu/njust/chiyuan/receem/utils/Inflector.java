package cn.edu.njust.chiyuan.receem.utils;

import java.util.*;
import java.util.regex.*;
/**
 * 
 *	单复数转换类
 * 2018年12月30日
 */
 
public class Inflector {
 
	private static final Pattern UNDERSCORE_PATTERN_1 = Pattern.compile("([A-Z]+)([A-Z][a-z])");
	private static final Pattern UNDERSCORE_PATTERN_2 = Pattern.compile("([a-z\\d])([A-Z])");
 
	private static List<RuleAndReplacement> plurals = new ArrayList<RuleAndReplacement>();
	private static List<RuleAndReplacement> singulars = new ArrayList<RuleAndReplacement>();
	private static List<String> uncountables = new ArrayList<String>();
 
	private static Inflector instance; 
 
	private Inflector() {
		initialize();
	}
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 单数转复数
		System.out.println(Inflector.getInstance().pluralize("water"));
		System.out.println(Inflector.getInstance().pluralize("box"));
		System.out.println(Inflector.getInstance().pluralize("tomato"));
		// 复数转单数
		System.out.println(Inflector.getInstance().singularize("apples"));
	}
	private void initialize() {
		plural("$", "s");
		plural("s$", "s");
		plural("(ax|test)is$", "$1es");
		plural("(octop|vir)us$", "$1i");
		plural("(alias|status)$", "$1es");
		plural("(bu)s$", "$1es");
		plural("(buffal|tomat)o$", "$1oes");
		plural("([ti])um$", "$1a");
		plural("sis$", "ses");
		plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
		plural("(hive)$", "$1s");
		plural("([^aeiouy]|qu)y$", "$1ies");
		plural("([^aeiouy]|qu)ies$", "$1y");
		plural("(x|ch|ss|sh)$", "$1es");
		plural("(matr|vert|ind)ix|ex$", "$1ices");
		plural("([m|l])ouse$", "$1ice");
		plural("(ox)$", "$1es");
		plural("(quiz)$", "$1zes");
 
		singular("s$", "");
		singular("(n)ews$", "$1ews");
		singular("([ti])a$", "$1um");
		singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
		singular("(^analy)ses$", "$1sis");
		singular("([^f])ves$", "$1fe");
		singular("(hive)s$", "$1");
		singular("(tive)s$", "$1");
		singular("([lr])ves$", "$1f");
		singular("([^aeiouy]|qu)ies$", "$1y");
		singular("(s)eries$", "$1eries");
		singular("(m)ovies$", "$1ovie");
		singular("(x|ch|ss|sh)es$", "$1");
		singular("([m|l])ice$", "$1ouse");
		singular("(bus)es$", "$1");
		singular("(o)es$", "$1");
		singular("(shoe)s$", "$1");
		singular("(cris|ax|test)es$", "$1is");
		singular("([octop|vir])i$", "$1us");
		singular("(alias|status)es$", "$1");
		singular("^(ox)es", "$1");
		singular("(vert|ind)ices$", "$1ex");
		singular("(matr)ices$", "$1ix");
		singular("(quiz)zes$", "$1");
 
		
 
		uncountable(new String[] { "equipment", "information", "rice", "money", "species", "series", "fish", "sheep" });
	}
 
	public static Inflector getInstance() {
		if (instance == null) {
			instance = new Inflector();
		}
		return instance;
	}
 
	public String underscore(String camelCasedWord) {
 
		String underscoredWord = UNDERSCORE_PATTERN_1.matcher(camelCasedWord).replaceAll("$1_$2");
		underscoredWord = UNDERSCORE_PATTERN_2.matcher(underscoredWord).replaceAll("$1_$2");
		underscoredWord = underscoredWord.replace('-', '_').toLowerCase();
 
		return underscoredWord;
	}
 
	public String pluralize(String word) {
		if (uncountables.contains(word.toLowerCase())) {
			return word;
		}
		return replaceWithFirstRule(word, plurals);
	}
 
	public String singularize(String word) {
		if (uncountables.contains(word.toLowerCase())) {
			return word;
		}
		return replaceWithFirstRule(word, singulars);
	}
 
	private String replaceWithFirstRule(String word, List<RuleAndReplacement> ruleAndReplacements) {
 
		for (RuleAndReplacement rar : ruleAndReplacements) {
			String rule = rar.getRule();
			String replacement = rar.getReplacement();
 
			// Return if we find a match.
			Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
			if (matcher.find()) {
				return matcher.replaceAll(replacement);
			}
		}
		return word;
	}
 
	public String tableize(String className) {
		return pluralize(underscore(className));
	}
 
	public String tableize(Class<?> klass) {
		String className = klass.getName().replace(klass.getPackage().getName() + ".", "");
		return tableize(className);
	}
 
	public static void plural(String rule, String replacement) {
		plurals.add(0, new RuleAndReplacement(rule, replacement));
	}
 
	public static void singular(String rule, String replacement) {
		singulars.add(0, new RuleAndReplacement(rule, replacement));
	}
 
	public static void irregular(String singular, String plural) {
		plural(singular, plural);
		singular(plural, singular);
	}
 
	public static void uncountable(String... words) {
		for (String word : words) {
			uncountables.add(word);
		}
	}
}
 
class RuleAndReplacement {
	private String rule;
	private String replacement;
 
	public RuleAndReplacement(String rule, String replacement) {
		this.rule = rule;
		this.replacement = replacement;
	}
 
	public String getReplacement() {
		return replacement;
	}
 
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
 
	public String getRule() {
		return rule;
	}
 
	public void setRule(String rule) {
		this.rule = rule;
	}
}
