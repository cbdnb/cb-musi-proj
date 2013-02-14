package de.dnb.music.mediumOfPerformance;

import java.util.Set;
import java.util.TreeSet;

import utils.Pair;
import utils.StringUtils;

/**
 * Enthält die Instrumente.
 * 
 * Außerdem ist einige Grundfunktionalität zum Abgleich mit dem Parsetring
 * vorhanden.
 * 
 * @author baumann
 *
 */
public final class InstrumentDB {

	private InstrumentDB() {
	}

	private static boolean uMusikErkennen = false;

	/*private*/static final int ABBR = 0, WR_OUT = 1, NID = 2, IDN = 3,
			SWD = 4, RAK_E_ALT = 5, RAK_U = 6;

	/**
	 * Eine schlichte Datenbank. Die Datensätze liegen als Zeilen einer 
	 * Tabelle vor:
	 * 
	 * Spaltenname:		Beschreibung:
	 * -----------------------------------------------------------------------
	 * ABBR			Bevorzugte Abkürzung nach RAK (Anh.) für Instrumente
	 * 					der E-Musik. Gibt es keine Ankürzung, so steht hier
	 * 					der ausgeschriebene Name
	 * WR_OUT		Ausgeschrieben nach SWD (für Titel)
	 * NID				die nid
	 * IDN				die idn (ppn)
	 * SWD				Die Vorzugsbenennung in der SWD, also mit eventuellen
	 * 					Homonymzusätzen
	 * RAK_E_ALT		Alternative Abkürzung, meist veraltet. 
	 * 					Gibt es mehrere Abkürzungen, so muss die Zeile
	 * 					für jede Abkürzung erneut in die Tabelle
	 * 					eingetragen werden.
	 * RAK_U			Die Abkürzung in der U-Musik. 
	 * 					Gibt es mehrere Abkürzungen, so muss die Zeile
	 * 					für jede Abkürzung erneut in die Tabelle
	 * 					eingetragen werden.		
	 * 
	 * Die letzten beiden Spalten müssen nicht verpflichtend ausgefüllt
	 * werden.
	 */
	/*private*/static final String[][] DATA =
		{
			{ "1stg.", "einstimmig", "4151426-9", "041514262",
				"Einstimmige Musik", "1stg", "" },
			{ "2stg.", "zweistimmig", "4191270-6", "041912705",
				"Zweistimmigkeit", "2stg", "" },
			{ "3stg.", "dreistimmig", "4400063-7", "946676933",
				"Dreistimmigkeit", "3stg", "" },
			{ "4stg.", "vierstimmig", "4400064-9", "946676941",
				"Vierstimmigkeit", "4stg", "" },
			{ "5stg.", "fünfstimmig", "4541247-9", "956290019",
				"Fünfstimmigkeit", "5stg", "" },
			{ "6stg.", "sechsstimmig", "7855026-9", "1019897279",
				"Sechsstimmigkeit", "6stg", "" },
			{ "7stg.", "siebenstimmig", "7855027-0", "1019898828",
				"Siebenstimmigkeit", "7stg", "" },
			{ "8stg.", "achtstimmig", "7855029-4", "1019901993",
				"Achtstimmigkeit", "8stg", "" },
			{ "9stg.", "neunstimmig", "7855030-0", "1019902124",
				"Neunstimmigkeit", "9stg", "" },
			{ "10stg.", "zehnstimmig", "7855031-2", "101990223X",
				"Zehnstimmigkeit", "10stg", "" },
			{ "11stg.", "elfstimmig", "7855032-4", "1019902361",
				"Elfstimmigkeit", "11stg", "" },
			{ "12stg.", "zwölfstimmig", "7855034-8", "1019902515",
				"Zwölfstimmigkeit", "12stg", "" },

			{ "Akk", "Akkordeon", "4122772-4", "041227727", "Akkordeon", "",
				"acc" },
			{ "Arp", "Arpeggione", "4794126-1", "972228004", "Arpeggione", "",
				"" },
			{ "Bc", "Basso continuo", "4020105-3", "040201058", "Generalbass",
				"", "" },
			{ "Blasorch", "Blasorchester", "4112784-5", "041127846",
				"Blasorchester", "", "" },
			{ "Blfl", "Blockflöte", "4146029-7", "041460294", "Blockflöte", "",
				"" },
			{ "Cel", "Celesta", "7694384-7", "100078388X", "Celesta", "", "cel" },
			{ "Cemb", "Cembalo", "4009667-1", "04009667X", "Cembalo", "", "" },
			{ "Ehr", "Englischhorn", "4152282-5", "041522826", "Englischhorn",
				"EHr", "engl-h" },
			{ "Ensemble", "Ensemble", "4212188-7", "042121884", "Ensemble", "",
				"" },
			{ "Fg", "Fagott", "4153531-5", "041535316", "Fagott", "", "" },
			{ "Fl", "Flöte", "4154647-7", "041546474", "Flöte", "", "fl" },
			{ "Fl d'amore", "Flûte d'Amour", "7604962-0", "987958224",
				"Flûte d'Amour", "", "" },
			{ "Git", "Gitarre", "4124933-1", "04124933X", "Gitarre", "", "g" },
			{ "Hf", "Harfe", "4129456-7", "041294564", "Harfe", "", "" },
			{ "Harm", "Harmonium", "4159135-5", "041591356", "Harmonium", "",
				"" },
			{ "Hr", "Horn", "4160662-0", "041606620", "Horn <Musikinstrument>",
				"", "" },
			{ "Klar", "Klarinette", "4030938-1", "04030938X", "Klarinette", "",
				"cl" },
			{ "linke Hand", "linke Hand", "4314658-2", "043146589",
				"Linke Hand", "linke Hd", "" },
			{ "4hdg.", "vierhändig", "4188277-5", "041882776",
				"Vierhändige Klaviermusik", "4hdg", "" },
			{ "3hdg.", "dreihändig", "4334858-0", "940696037",
				"Sechs- und mehrhändige Klaviermusik", "3hdg", "" },
			{ "6hdg.", "sechshändig", "4334858-0", "940696037",
				"Sechs- und mehrhändige Klaviermusik", "6hdg", "" },
			{ "8hdg.", "achthändig", "4334858-0", "940696037",
				"Sechs- und mehrhändige Klaviermusik", "8hdg", "" },
			{ "10hdg.", "zehnhändig", "4334858-0", "940696037",
				"Sechs- und mehrhändige Klaviermusik", "10hdg", "" },
			{ "Kl", "Klavier", "4030982-4", "040309827", "Klavier", "", "p" },
			{ "Kb", "Kontrabass", "4032300-6", "040323005", "Kontrabass", "",
				"b" },
			{ "Lt", "Laute", "4166936-8", "041669363", "Laute", "", "lt" },
			{ "Md", "Mandoline", "4232939-5", "042329396", "Mandoline", "",
				"mand" },
			{ "Ob", "Oboe", "4139391-0", "041393910", "Oboe", "", "" },
			{ "Ob d'amore", "Oboe d'amore", "4172323-5", "041723236",
				"Oboe d'amore", "", "" },
			{ "Orch", "Orchester", "4172708-3", "041727088", "Orchester", "",
				"" },
			{ "Org", "Orgel", "4043844-2", "040438449", "Orgel", "", "org" },
			{ "Pk", "Pauke", "4173567-5", "041735676", "Pauke", "", "" },
			{ "Pikkolofl", "Pikkoloflöte", "4197172-3", "041971728",
				"Pikkoloflöte", "", "" },
			{ "Pos", "Posaune", "4046865-3", "040468658", "Posaune", "", "tb" },
			{ "Sax", "Saxophon", "4179250-6", "041792505", "Saxophon", "",
				"sax" },
			{ "Schz", "Schlagzeug", "4137284-0", "041372840", "Schlagzeug",
				"Schlaginstr", "perc" },
			{ "Streichorch", "Streichorchester", "4183630-3", "041836308",
				"Streichorchester", "", "" },
			{ "Synth", "Synthesizer", "4184253-4", "041842537", "Synthesizer",
				"", "synth" },
			//Tasteninstr nach statist. Analyse
			{ "Tasteninstr.", "Tasteninstrument", "4059103-7", "040591034",
				"Tasteninstrument", "Tasteninstr", "" },
			{ "Trp", "Trompete", "4060993-5", "040609936", "Trompete", "", "tp" },
			{ "Tb", "Tuba", "4061130-9", "040611302", "Tuba", "", "tu" },
			{ "Vib", "Vibraphon", "4188204-0", "041882040", "Vibraphon", "",
				"vib" },
			{ "Va", "Viola", "4188364-0", "041883640", "Viola", "", "" },
			{ "Va d'amore", "Viola d'amore", "4127764-8", "041277643",
				"Viola d'amore", "", "" },
			{ "Vagb", "Viola da gamba", "4127766-1", "04127766X",
				"Viola da gamba", "", "" },
			{ "Vl", "Violine", "4019791-8", "040197913", "Violine", "", "v" },
			{ "Vc", "Violoncello", "4063584-3", "040635848", "Violoncello", "",
				"" },
			{ "Xyl", "Xylophon", "4190396-1", "04190396X", "Xylophon", "",
				"xyl" },
			{ "Zth", "Zither", "4067892-1", "04067892X", "Zither", "", "" },
			{ "Sopr-Instr.", "Sopraninstrument", "7694421-9", "1000788717",
				"Sopraninstrument", "Sopr-Instr", "" },
			{ "Alt-Instr.", "Altinstrument", "7694423-2", "1000788857",
				"Altinstrument", "Alt-Instr", "" },
			{ "Ten-Instr.", "Tenorinstrument", "7694424-4", "1000788938",
				"Tenorinstrument", "Ten-Instr", "" },
			// Bass-Instr und Baß-Instr. nach statist. Analyse
			{ "Bass-Instr.", "Bassinstrument", "7694425-6", "1000789012",
				"Bassinstrument", "Baß-Instr.", "" },
			{ "Bass-Instr.", "Bassinstrument", "7694425-6", "1000789012",
				"Bassinstrument", "Bass-Instr", "" },
			{ "E-Kl", "Elektrisches Klavier", "4232228-5", "042322286",
				"Elektrisches Klavier", "E-Cel", "el-cel" },
			{ "E-Kl", "Elektrisches Klavier", "4232228-5", "042322286",
				"Elektrisches Klavier", "", "el-p" },
			{ "E-Fl", "Elektrische Flöte", "7694515-7", "1000800814",
				"Elektrische Flöte", "", "el-fl" },
			{ "E-Git", "Elektrogitarre", "4151804-4", "041518047",
				"Elektrogitarre", "", "el-g" },
			{ "E-Hf", "Elektrische Harfe", "7694517-0", "100080092X",
				"Elektrische Harfe", "", "" },
			{ "E-Kb", "Elektrischer Kontrabass", "7694518-2", "1000801209",
				"Elektrischer Kontrabass", "", "" },
			{ "E-Klar", "Elektrische Klarinette", "7694521-2", "1000801403",
				"Elektrische Klarinette", "", "el-cl" },
			{ "E-Md", "Elektrische Mandoline", "7694532-7", "1000801969",
				"Elektrische Mandoline", "", "el-mand" },
			{ "E-Org", "Elektronenorgel", "4151880-9", "041518802",
				"Elektronenorgel", "", "el-org" },
			{ "E-Schz", "Elektronisches Schlagzeug", "4226169-7", "042261694",
				"Elektronisches Schlagzeug", "", "el-dr" },
			{ "E-Schz", "Elektronisches Schlagzeug", "4226169-7", "042261694",
				"Elektronisches Schlagzeug", "", "el-perc" },
			{ "E-Sax", "Elektrisches Saxophon", "7694534-0", "1000802108",
				"Elektrisches Saxophon", "", "el-sax" },
			{ "E-Pos", "Elektrische Posaune", "7694535-2", "1000802159",
				"Elektrische Posaune", "", "el-tb" },
			{ "E-Trp", "Elektrische Trompete", "7694536-4", "1000802299",
				"Elektrische Trompete", "", "el-tp" },
			{ "E-Vl", "Elektronische Geige", "7694541-8", "1000802434",
				"Elektronische Geige", "", "el-v" },
			{ "E-Tasteninstr.", "Elektronisches Tasteninstrument", "7694546-7",
				"1000802736", "Elektronisches Tasteninstrument", "", "" },
			{ "E-Vc", "Elektrisches Cello", "7694547-9", "1000802981",
				"Elektrisches Cello", "", "" },
			{ "Alt", "Alt", "4270768-7", "042707684", "Alt <Stimmlage>", "", "" },
			{ "Bar", "Bariton", "4392006-8", "946059047",
				"Bariton <Stimmlage>", "", "" },
			{ "Bass", "Bass", "4712215-8", "965896897", "Bass <Stimmlage>",
				"Baß", "" },
			{ "Bassbar", "Bassbariton", "7694552-2", "1000803538",
				"Bassbariton <Stimmlage>", "Baßbar", "" },
			{ "Chor", "Chor", "4010045-5", "040100456", "Chor", "", "" },
			{ "Counterten", "Countertenor", "4148299-2", "041482999",
				"Countertenor <Stimmlage>", "Counterten.", "" },
			{ "Frauenchor", "Frauenchor", "4310304-2", "043103049",
				"Frauenchor", "", "" },
			{ "Frauenst.", "Frauenstimme", "4235155-8", "042351553",
				"Frauenstimme", "Frauenst", "" },
			{ "Kinderchor", "Kinderchor", "4163811-6", "041638115",
				"Kinderchor", "", "" },
			{ "Knabenchor", "Knabenchor", "4289869-9", "042898692",
				"Knabenchor", "", "" },
			{ "Männerchor", "Männerchor", "4168467-9", "041684672",
				"Männerchor", "", "" },
			{ "Männerst.", "Männerstimme", "4644400-2", "96205870X",
				"Männerstimme", "Männerst", "" },
			{ "Mezzosopr", "Mezzosopran", "4425470-2", "948695250",
				"Mezzosopran", "Msopr", "" },
			{ "Sopr", "Sopran", "4392005-6", "946059004", "Sopran", "", "" },
			{ "Sprechst.", "Stimme", "4057587-1", "04057587X", "Stimme",
				"Stimme", "" },
			{ "Ten", "Tenor", "4184718-0", "041847180", "Tenor <Stimmlage>",
				"", "" },
			{ "Singst.", "Singstimme", "4156941-6", "041569415",
				"Gesangsstimme", "Singst", "voc" },
			{ "Singst.", "Singstimme", "4156941-6", "041569415",
				"Gesangsstimme", "Gesangstimme", "voc" },
			{ "Alphorn", "Alphorn", "4142016-0", "041420160", "Alphorn", "", "" },
			{ "Bajan", "Bajan", "4432494-7", "949180157", "Bajan", "", "" },
			{ "Balalaika", "Balalaika", "4143930-2", "041439309", "Balalaika",
				"", "" },
			{ "Bandoneon", "Bandoneon", "4197338-0", "041973380", "Bandoneon",
				"Bandonion", "" },
			{ "Baryton", "Baryton", "4144084-5", "041440846", "Baryton", "", "" },
			{ "Bassetthr", "Bassetthorn", "4391544-9", "946033757",
				"Bassetthorn", "", "" },
			{ "Bassklar", "Bassklarinette", "4414463-5", "947894136",
				"Bassklarinette", "", "" },
			{ "Buk", "Buk", "7744549-1", "1011504286", "Buk <Trommel>", "", "" },
			{ "Calchedon", "Calchedon", "7662012-8", "994721102", "Calchedon",
				"", "" },
			{ "Chalumeau", "Chalumeau", "4231682-0", "042316820", "Chalumeau",
				"", "" },
			{ "Chromonika", "Chromonika", "4520998-4", "954957113",
				"Chromatische Mundharmonika", "", "" },
			{ "Clarinhorn", "Clarinhorn", "7744553-3", "1011504545",
				"Clarinhorn", "", "" },
			{ "Colascione", "Colascione", "7744554-5", "1011504707",
				"Colascione", "", "" },
			{ "Concertina", "Concertina", "4197339-2", "041973399",
				"Konzertina", "", "" },
			{ "Czakan", "Czakan", "4299918-2", "042999189", "Stockflöte", "",
				"" },
			{ "Domra", "Domra", "4627729-8", "961012617", "Domra", "", "" },
			{ "Drehleier", "Drehleier", "4131197-8", "041311973", "Drehleier",
				"", "" },
			{ "Dudelsack", "Dudelsack", "4178853-9", "041788532", "Sackpfeife",
				"", "" },
			{ "Elektronische Klänge", "Elektronische Klänge", "4014362-4",
				"040143627", "Elektronisches Musikinstrument", "", "" },
			{ "Euphonium", "Euphonium", "4242467-7", "042424674",
				"Bariton <Musikinstrument>", "", "" },
			// Flautello und Flautino nach SWD synonym, aber nicht nach RAK
			{ "Flautello", "Flautello", "7744555-7", "1011504871", "Flautino",
				"", "" },
			{ "Flautino", "Flautino", "7744555-7", "1011504871", "Flautino",
				"", "" },
			{ "Flügelhr", "Flügelhorn", "4649777-8", "962344753", "Flügelhorn",
				"", "" },
			{ "Geisha-Glocke", "Geisha-Glocke", "4507643-1", "954015436",
				"Handglocke", "", "" },
			{ "Glashf", "Glasharfe", "4756294-8", "969357362", "Glasharfe", "",
				"" },
			{ "Glashmk", "Glasharmonika", "4603491-2", "959699961",
				"Glasharmonika", "", "" },
			{ "Glockenspiel", "Glockenspiel", "4021295-6", "040212955",
				"Glockenspiel", "", "" },
			{ "Hackbrett", "Hackbrett", "4158657-8", "041586573", "Hackbrett",
				"", "" },
			{ "Harmonika", "Harmonika", "4541301-0", "956293522", "Harmonika",
				"", "" },
			{ "Heckelphon", "Heckelphon", "4442805-4", "949788708",
				"Heckelphon", "", "" },
			{ "Kayagum", "Kayagum", "7611153-2", "988613425", "Kayagum", "", "" },
			{ "Kornett", "Kornett", "4197145-0", "041971450", "Kornett", "",
				"co" },
			{ "Live-Elektronik", "Live-Elektronik", "4014362-4", "040143627",
				"Elektronisches Musikinstrument", "", "" },
			{ "Mandola", "Mandola", "4168776-0", "041687760", "Mandola", "", "" },
			// Abkürzung wäre korrekt Mar
			{ "Marimba", "Marimba", "4168894-6", "041688945", "Marimba", "",
				"ma" },
			{ "Marimbaphon", "Marimbaphon", "4168895-8", "041688953",
				"Marimbaphon", "", "" },
			{ "Maultrommel", "Maultrommel", "4209622-4", "042096227",
				"Maultrommel", "", "" },
			{ "Melodika", "Melodika", "4169386-3", "041693868", "Melodica", "",
				"" },
			{ "Metallophon", "Metallophon", "7744556-9", "1011505215",
				"Metallophon", "", "" },
			{ "Hmk", "Mundharmonika", "4123805-9", "041238052",
				"Mundharmonika", "Mundhmk", "harm" },
			{ "Musette", "Musette", "4178853-9", "041788532", "Sackpfeife", "",
				"" },
			{ "Ondes Martenot", "Ondes Martenot", "4447637-1", "950031860",
				"Ondes Martenot", "", "" },
			{ "Panfl", "Panflöte", "4044462-4", "040444627", "Panflöte", "", "" },
			{ "Pipa", "Pipa", "4315916-3", "043159168", "Pipa", "", "" },
			{ "Psalterium", "Psalterium", "4197387-2", "041973879",
				"Psalterium", "", "" },
			{ "Sheng", "Sheng", "4170754-0", "041707540", "Mundorgel", "", "" },
			{ "Sho", "Sho", "4170754-0", "041707540", "Mundorgel", "", "" },
			{ "Sitar", "Sitar", "4181587-7", "041815874", "Sitar", "", "" },
			{ "Sopranino", "Sopranino", "7744557-0", "1011505339", "Sopranino",
				"", "" },
			{ "Sprech", "Sprecher", "4182545-7", "041825454", "Sprecher", "",
				"" },
			{ "Theorbe", "Theorbe", "4185092-0", "041850920", "Theorbe", "", "" },
			{ "Tonband", "Tonband", "4135867-3", "041358678", "Tonband", "", "" },
			{ "Trautonium", "Trautonium", "4549556-7", "956704387",
				"Trautonium", "", "" },
			{ "Triangel", "Triangel", "4801084-4", "972618783", "Triangel", "",
				"" },
			{ "Trommel", "Trommel", "4117255-3", "041172558", "Trommel", "",
				"dr" },
			{ "Violetta", "Violetta", "7744558-2", "1011505398", "Violetta",
				"", "" },
			{ "Violone", "Violone", "4524780-8", "955277752", "Violone", "", "" },
			{ "Yokobue", "Yokobue", "7744559-4", "1011505509", "Yokobue", "",
				"" },
			{ "Zheng", "Zheng", "4309024-2", "043090249", "Zheng", "", "" },
			{ "Zupforch", "Zupforchester", "4323707-1", "04323707X",
				"Zupforchester", "", "" },

			{ "Elektrobass", "Elektrobass", "4261650-5", "042616506",
				"Elektrobass", "", "el-b" },
			{ "Elektrisches Banjo", "Elektrisches Banjo", "7694524-8",
				"1000801594", "Elektrisches Banjo", "", "el-bj" },
			{ "Altsaxophon", "Altsaxophon", "7694553-4", "1000803961",
				"Altsaxophon", "", "as" },
			{ "Sopransaxophon", "Sopransaxophon", "7694554-6", "1000804089",
				"Sopransaxophon", "", "ss" },
			{ "Tenorsaxophon", "Tenorsaxophon", "7694555-8", "1000804143",
				"Tenorsaxophon", "", "ts" },
			{ "Baritonsaxophon", "Baritonsaxophon", "7694558-3", "1000804186",
				"Baritonsaxophon", "", "bs" },
			{ "Banjo", "Banjo", "4267789-0", "042677890", "Banjo", "", "bj" },
			{ "Bongo", "Bongo", "4716320-3", "966254287", "Bongo", "", "bo" },
			{ "Conga", "Congatrommel", "4224188-1", "04224188X",
				"Congatrommel", "", "cga" },
			{ "Waldhorn", "Waldhorn", "4188979-4", "041889797", "Waldhorn", "",
				"h" },
			{ "Waldhorn", "Waldhorn", "4188979-4", "041889797", "Waldhorn", "",
				"fr-h" },
			{ "Keyboard", "Keyboard", "4245707-5", "042457076", "Keyboard", "",
				"keys" },
			{ "Bandleader", "Bandleader", "7628944-8", "990720039",
				"Bandleader", "", "ld" },
			{ "Washboard", "Washboard", "7694575-3", "100080996X", "Washboard",
				"", "wbd" }

		};

	/**
	 * Erwartet einen String nicht null.
	 * 
	 * Sucht nach dem längsten Präfix von parseString, das in der Datenbank der 
	 * Abkürzungen und ausgeschriebenen Fassungen enthalten ist.
	 * 
	 * Liefert im Erfolgsfall ein passendes Instruments, sonst null. 
	 * Match und Rest sind in den entsprechenden Feldern von Instrument 
	 * gespeichert. Die Suche erfolgt linear im Feld DATA.
	 * 
	 * Der globale Schalter uMusikErkennen (zu setzen über 
	 * setUMusikErkennen(boolean)) steuert, ob Instrumente der U-Musik (in
	 * der Regel klein geschrieben) erkannt werden. Diese Instrumente finden
	 * sich in der Spalte RAK_U von DATA.
	 * 
	 * @param parseString String zu parsen.
	 * @return		ein Instrument oder null
	 */
	public static Instrument matchInstrument(final String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an matchInstrument()übergeben");

		//@formatter:off
		Pair<String, String[]> slp =
			uMusikErkennen 
			? 
				StringUtils.findLongestPrefix(
						parseString, DATA, 
						ABBR, WR_OUT, RAK_E_ALT, RAK_U) 
			: 
				StringUtils.findLongestPrefix(
						parseString, DATA, ABBR, WR_OUT, RAK_E_ALT);
		//@formatter:on
		if (slp != null) {

			String[] line = slp.second;
			String match = slp.first;

			Instrument ins = buildInstrument(line);
			ins.rest = parseString.substring(match.length());

			return ins;

		}

		return null;

	}

	/**
	 * @param line
	 * @return
	 */
	private static Instrument buildInstrument(String[] line) {
		Instrument ins = new Instrument();

		ins.writtenOut = line[WR_OUT];
		ins.abbreviated = line[ABBR];
		ins.nid = line[NID];
		ins.idn = line[IDN];
		ins.swd = line[SWD];
		return ins;
	}

	public static void setRegnognizePopularMusic(final boolean musikErkennen) {
		uMusikErkennen = musikErkennen;
	}
	
	public static Set<Instrument> getAllInstruments(){
		Set<Instrument> insSet = new TreeSet<Instrument>();
		for (String[] instrumentS : DATA) {
			insSet.add(buildInstrument(instrumentS));
		}
		return insSet;
	}

	public static void main(String[] args) {
		for (String[] stringA : DATA) {
			System.err.println(getAllInstruments());

		}
	}

}
