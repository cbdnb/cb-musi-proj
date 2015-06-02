package de.dnb.music.additionalInformation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import de.dnb.music.utils.StringUtils;

public final class ThematicIndexDB {

	private ThematicIndexDB() {
	}

	public static final int NAME = 0, THEMATIC_INDEX = 1, SOURCE = 2,
			SOURCE_ABB = 3, IDN = 4, COUNTRY_CODE = 5;

	/*private*/static final String[][] DATA =
		{

			// in der Form: Name, WV-Abkürzung, Bibl. Nachweis, Abk. nach Liste NSW
			{
				"Abel, Carl Friedrich",
				"K",
				"Knape, Walter: Bibliographisch-thematisches Verzeichnis der Kompositionen von Karl Friedrich Abel : (1723 - 1787) / Walter Knape - Cuxhaven : Selbstverl., [1972]. - X, 299 S. - Ill., Noten. - 30 cm IDN 730471519",
				"AbelWV", "118646583", "XA-DE;XA-GB" },
			{
				"Adlgasser, Anton Cajetan",
				"D",
				"De Catanzaro, Christine D. : Anton Cajetan Adlgasser : (1729 - 1777) ; a thematic catalogue of his works / by Christine D. De Catanzaro and Werner Rainer. - Hillsdale, NY : Pendragon Press, 2000. - XL, 311 S., Ill., Noten. - 26 cm (Thematic catalogues ; 22). - (Thematic catalogues series ; no. 22)\nISBN 1-945193-78-5\nIDN 963677136",
				"AdlgasserWV", "119024586", "XA-AT;XA-DE" },
			{
				"Albéniz, Isaac",
				"T",
				"Torres, Jacinto: Catálogo sistemático descriptivo de las obras musicales de Isaac Albéniz. - Madrid : Instituto de Bibliografía Musical, 2001.",
				"AlbenizWV", "119183366", "XA-ES" },
			{
				"Albrechtsberger, Johann Georg",
				"S",
				"Schröder, Dorothea: Die geistlichen Vokalkompositionen Johann Georg Albrechtsbergers / Dorothea Schröder. - Hamburg : Verl. der Musikalienhandlung Wagner. - 21 cm. -\nZugl.: Hamburg, Univ., Diss., 1986\nIDN 551317426\n(Hamburger Beiträge zur Musikwissenschaft ; 34)\n2. Thematischer Katalog. - 1987. - VIII, 370 S., Noten\nISBN 3-88979-026-7\nIDN 871097729",
				"AlbrechtsbergerWV", "118501704", "XA-AT" },
			{
				"Auber, Daniel-François-Esprit",
				"AWV",
				"Schneider, Herbert: Chronologisch-thematisches Verzeichnis sämtlicher Werke von Daniel François Esprit Auber : (AWV) / Herbert Schneider. - Hildesheim [u.a.] : Olms. - 25 cm\n(Musikwissenschaftliche Publikationen ; ...)\nIDN 941585743\nBd. 1. - 1994. - VII, 796 S., Noten\n(... ; 1,1)\nISBN 3-487-09867-9\nIDN 941586103\nBd. 2. - 1994. - S. 797 - 1708, Noten\n(... ; 1,2)\nISBN 3-487-09868-7\nIDN 941586111",
				"AuberWV", "118646192", "XA-FR" },

			{
				"Bach, Carl Philipp Emanuel",
				"Wq",
				"Wotquenne, Alfred: Thematisches Verzeichnis der Werke von Carl Philipp Emanuel Bach / hrsg. von Alfred Wotquenne . - Leipzig : Breitkopf & Härtel, 1905. - 69 S.\nPT: Catalogue thématique des oeuvres de Charles Philippe Emmanuel Bach <1714-1788>\nPPN 513748512",
				"Wq", "118505505", "XA-DE" },
			{
				"Bach, Johann Christian",
				"W",
				"Warburton, Ernest: Thematic catalogue. - New York [u.a.] : Garland, 1999. - VIII, 611 S., Noten\nISBN 0-8240-6097-0\nIDN 979295769\n(The collected works of Johann Christian Bach ; vol. 48, p. 1)\nIDN 560846541",
				"JCBachWV", "118505521", "XA-DE;XA-GB" },
			{
				"Bach, Johann Christoph Friedrich",
				"JCFB",
				"Ulrich Leisinger: Johann Christoph Friedrich Bach : thematisch-systematisches Verzeichnis der musikalischen Werke (BR-JCFB) / bearb. von Ulrich Leisinger. - Stuttgart : Carus-Verl., 2013. - (Bach-Repertorium ; Bd. 4).",
				"JCFB", "118651471", "XA-DE" },
			{
				"Bach, Johann Sebastian",
				"BWV",
				"Schmieder, Wolfgang: Thematisch-systematisches Verzeichnis der musikalischen Werke von Johann Sebastian Bach : Bach-Werke-Verzeichnis ; (BWV) / hrsg. von Wolfgang Schmieder. - 2. Überarb. und erw. Ausg. - Wiesbaden : Breitkopf & Härtel, 1990. - XLVI, 1014 S., zahlr. Noten. - 28 cm\nNebent.: J. S. Bach\nISBN 3-7651-00255-5\nIDN 910299250",
				"BWV", "11850553X", "XA-DE" },
			{
				"Bach, Wilhelm Friedemann",
				"F",
				"Falck, Martin: Wilhelm Friedemann Bach : sein Leben und seine Werke ; mit thematischem Verzeichnis seiner Kompositionen und zwei Bildern / von Martin Falck. - 2. Aufl. - Leipzig : Kahnt, 1919. -\nIV, 170, 31 S. : zahlr. Notenbeisp. \n(Studien zur Musikgeschichte ; 1)\nPPN 310929067",
				"WFBachWV", "118505548", "XA-DE" },

			{
				"Bach, Wilhelm Friedemann",
				"WFB",
				"Peter Wollny: Wilhelm Friedemann Bach : thematisch-systematisches Verzeichnis der musikalischen Werke (BR-WFB) / bearb. von Peter Wollny. - Stuttgart : Carus-Verl., 2012. - (Bach-Repertorium ; Bd. 2).",
				"WFB", "118505548", "XA-DE" },
			{
				"Bartók, Béla",
				"Sz",
				"Szabolcsi, Bence: Béla Bartók : Weg und Werk, Schriften und Briefe / zusammengestellt von Bence Szabolcsi. [Beitr. von ... Ausw. d. Photogr.: Janos Demny]. - Bonn : Boosey & Hawkes, 1957. - 371 S.\nNebent.: Bartók\nIDN 450240894",
				"BartokWV", "118506900", "XA-HU" },

			{ "Bartók, Béla", "BB", "???", "???", "118506900", "XA-HU" },
			{
				"Beethoven, Ludwig {van",
				"op./WoO/K",
				"Kinsky, Georg: Das Werk Beethovens : thematisch-bibliographisches Verzeichnis seiner sämtlichen vollendeten Kompositionen / Georg Kinsky. Nach d. Tode des Verfassers abgeschlossen und hrsg. von Hans Halm. - München [u.a.] : Henle, 1955. - XXII, 808 S., Noten\nNebent.: Beethovenverzeichnis\nIDN 452411246",
				"BeethovenWV", "118508288", "XA-DE;XA-AT" },
			{
				"Benda, Franz",
				"L",
				"Lee, Douglas A.: Franz Benda : (1709 - 1786) ; a thematic catalogue of his works / by Douglas A. Lee. - New York : Pendragon Press, 1984. - XXII, 221 S., 21 cm\n(Thematic catalogues, no. 10)\nISBN 0-918728-42-8\nIDN 974788279",
				"BendaWV", "119326728", "XA-AT;XA-DE" },
			{
				"Berlioz, Hector",
				"H",
				"Holoman, D. Kern: Catalogue of the works of Hector Berlioz / by D. Kern Holoman. - Kassel [u.a.] : Bärenreiter, 1987. - XLV, 527 S., Noten\n(New edition of the complete works / Hector Berlioz ; 25)\nISBN 3-7618-0449-0\nIDN 979298547",
				"BerliozWV", "118509675", "XA-FR" },
			{
				"Bliss, Arthur",
				"F",
				"Foreman, Lewis: Arthur Bliss : catalogue of the complete works / by Lewis Foreman. with an introduction by George Dannatt. - Sevenoaks : Novello, 1980. - 159 S. - 28 cm\nISBN 0-85360-069-4\nIDN 974773115",
				"BlissWV", "118851977", "XA-GB" },
			{
				"Boccherini, Luigi",
				"G",
				"Gérard, Yves: Thematic, bibliographical and critical catalogue of the works of Luigi Boccherini / comp. by Yves Gérard under the auspices of Germaine de Rotschild. Transl. by Andreas Mayor. - London [u.a.] : Oxford Univ. Press, 1969. - 716 S. - 25 cm\nIDN 974809365",
				"BoccheriniWV", "118659170", "XA-IT;XA-ES" },
			{
				"Brahms, Johannes",
				"op./WoO/M",
				"McCorkle, Margit L.: Johannes Brahms : thematisch-bibliographisches Werkverzeichnis / von Margit L. McCorkle. Hrsg. nach gemeinsamen Vorarbeiten mit Donald M. McCorkle. - München : Henle, 1984. - LXXVII, 841 S., 1 Ill., Noten. - 27 cm\nISBN 3-87328-041-8\nIDN 840811934",
				"BrahmsWV", "118514253", "XA-DE;XA-AT" },
			{
				"Bridge, Frank",
				"H",
				"Hindmarsh, Paul: Frank Bridge : a thematic catalogue 1900 - 1941 / Paul Hindmarsh. - London : Faber [u.a.], 1983. - 185 S. - 24 cm\nISBN 0-571-10032-5\nIDN 974780502",
				"BridgeWV", "118857142", "XA-GB" },
			{
				"Britten, Benjamin",
				"op.",
				"Benjamin Britten : a complete catalogue of his published works. - London : Boosey & Hawkes [u.a.], 1973. - 52 S., 1 Ill., Noten. - 1 Beil.\nIDN 560846916",
				"BrittenWV", "118515527", "XA-GB" },
			{
				"Bruckner, Anton",
				"WAB",
				"Grasberger, Renate: Werkverzeichnis Anton Bruckner : (WAB) / Renate Grasberger. - Tutzing : Schneider, 1977. - X, 309 S., Noten. - 24 cm\n(Publikationen des Instituts für Österreichische Musikdokumentation ; 7)\nISBN 3-7952-0232-9\nIDN 770497454",
				"WAB", "118515799", "XA-AT" },
			{ // alt
				"Bruckner, Anton",
				"G",
				"Grasberger, Renate: Werkverzeichnis Anton Bruckner : (WAB) / Renate Grasberger. - Tutzing : Schneider, 1977. - X, 309 S., Noten. - 24 cm\n(Publikationen des Instituts für Österreichische Musikdokumentation ; 7)\nISBN 3-7952-0232-9\nIDN 770497454",
				"WAB", "118515799", "XA-AT" },
			{
				"Busoni, Ferruccio",
				"K",
				"Kindermann, Jürgen: Thematisch-chronologisches Verzeichnis der musikalischen Werke von Ferruccio B. Busoni / von Jürgen Kindermann. - Regensburg : Bosse, 1980. - 518 S. - 25 cm\n(Studien zur Musikgeschichte des 19. Jahrhunderts ; 19)\nISBN 3-7649-2033-5\nIDN 810070502",
				"BusoniWV", "118518011", "XA-IT;XA-DE" },
			{
				"Buxtehude, Dietrich",
				"BuxWV",
				"Karstädt, Georg: Thematisch-systematisches Verzeichnis der musikalischen Werke von Dietrich Buxtehude : Buxtehude-Werke-Verzeichnis ; [BuxWV] / hrsg. von Georg Karstädt. - 2. erw. und verb. Aufl. - Wiesbaden : Breitkopf & Härtel, 1985. - XVIII, 246 S. - 27 cm\nISBN 3-7651-0065-X\nIDN 97480942X",
				"BuxWV", "118665685", "XA-DE" },
			{ // alt
				"Buxtehude, Dietrich",
				"K",
				"Karstädt, Georg: Thematisch-systematisches Verzeichnis der musikalischen Werke von Dietrich Buxtehude : Buxtehude-Werke-Verzeichnis ; [BuxWV] / hrsg. von Georg Karstädt. - 2. erw. und verb. Aufl. - Wiesbaden : Breitkopf & Härtel, 1985. - XVIII, 246 S. - 27 cm\nISBN 3-7651-0065-X\nIDN 97480942X",
				"BuxWV", "118665685", "XA-DE" },

			//			{
			//				"ÿéCajkovskij, Pëtr I.",
			//				"ÿéCS",
			//				"Tematiko-bibliografičeskij ukazatel’ sočinenij P. I. Čajkovskogo = Thematic and bibliographical catalogue of P. I. Tchaikovsky’s works / red. Polina Vajdman … - Moscow : Jurgenson, 2006. - [(New edition of the complete works / Pëtr I. Čajkovskij ; Ser. 12)]",
			//				"ČajkovskijWV", "118638157", "XA-RU" },
			//			{
			//				"ÿéCajkovskij, Pëtr I.",
			//				"ČS",
			//				"Tematiko-bibliografičeskij ukazatel’ sočinenij P. I. Čajkovskogo = Thematic and bibliographical catalogue of P. I. Tchaikovsky’s works / red. Polina Vajdman … - Moscow : Jurgenson, 2006. - [(New edition of the complete works / Pëtr I. Čajkovskij ; Ser. 12)]",
			//				"ČajkovskijWV", "118638157", "XA-RU" },

			//???
			//			{
			//				"Čajkovskij, Pe͏̈tr I.",
			//				"ÿéCS",
			//				"Tematiko-bibliografičeskij ukazatel’ sočinenij P. I. Čajkovskogo = Thematic and bibliographical catalogue of P. I. Tchaikovsky’s works / red. Polina Vajdman … - Moscow : Jurgenson, 2006. - [(New edition of the complete works / Pëtr I. Čajkovskij ; Ser. 12)]",
			//				"ČajkovskijWV", "118638157", "XA-RU" },
			{
				"Čajkovskij, Pëtr Ilʹič",
				"ČS",
				"Tematiko-bibliografičeskij ukazatel’ sočinenij P. I. Čajkovskogo = Thematic and bibliographical catalogue of P. I. Tchaikovsky’s works / red. Polina Vajdman … - Moscow : Jurgenson, 2006. - [(New edition of the complete works / Pëtr I. Čajkovskij ; Ser. 12)]",
				"ČajkovskijWV", "118638157", "XA-RU" },

			{
				"Carulli, Ferdinando",
				"op.",
				"Torta, Mario: Catalogo tematico delle opere di Ferdinando Carulli / Mario Torta. - Lucca : Libreria Musicale Italiana, 1993. - 21 cm\n(Musicalia ; )\nISBN 88-7096-036-6\nIDN 560851219\n1. - 1994. - ( ; 3) 2. - 1993. - ( ; 3) Zählung Maßgebliches Verzeichnis",
				"CarulliWV", "119321203", "XA-IT" },
			{
				"Charpentier, Marc-Antoine",
				"H",
				"Hitchcock, Hugh Wiley: Les oevres de Marc-Antoine Charpentier : catalogue raisonné H. W. Hitchcock. [Vorw.: Norbert Dufourcq]. - Paris : Picard, 1982. - 419 S. - 27 cm\n(La vie musicale en France sous les rois bourbons)\nISBN 2-7084-0084-3\nIDN 974780677",
				"CharpentierWV", "118675648", "XA-FR" },
			{
				"Chopin, Frédéric",
				"op./K",
				"Kobylanska, Krystyna: Frédéric Chopin : thematisch-bibliographisches Werkverzeichnis / von Krystyna Kobylanska. [Übers. d. poln. Orig. Helmut Stolze]. - München : Henle, 1979. - XXII, 362 S. - 1 Ill. (farb.), Noten. - 27 cm\nEST: Rekopisy utworw Chopina katalog <dt.>\nNebent.: Chopin\nISBN 3-87328-029-9\nIDN 800380533",
				"ChopinWV", "118520539", "XA-PL;XA-FR" },
			{
				"Clementi, Muzio",
				"op.",
				"Tyson, Alan: Thematic catalogue of the works of Muzio Clementi / Alan Tyson. - Tutzing : Schneider, 1967. - 136 S.\nNebent.: Muzio Clementi, thematic catalogue\nIDN 458453013",
				"ClementiWV", "118676423", "XA-IT" },
			{
				"Corelli, Arcangelo",
				"op./WoO/M",
				"Marx, Hans Joachim: Die Überlieferung der Werke Arcangelo Corellis : catalogue raisonné / von Hans Joachim Marx. - Köln : Volk, 1980. - 356 S. - 25 cm\n(Historisch-kritische Gesamtausgabe der musikalischen Werke / Arcangelo Corelli ; Suppl.-Bd.)\nISBN 3-87252-121-7\nIDN 974791059",
				"CorelliWV", "11901176X", "XA-IT" },
			{
				"Cornelius, Peter",
				"W",
				"Wagner, Günter: Peter Cornelius : Verzeichnis seiner musikalischen und literarischen Werke / Günter Wagner. - Tutzing : Schneider, 1986. - 532 S., Noten. - 31 cm\n(Mainzer Studien zur Musikwissenschaft ; 13)\nISBN 3-7952-0455-0\nIDN 870503979",
				"CorneliusWV", "118522213", "XA-DE;XA-AT" },
			{
				"Danzi, Franz",
				"P",
				"Pechstaedt, Volkmar von: Thematisches Verzeichnis der Kompositionen von Franz Danzi : (1763 - 1826) ; mit einem Anhang der literarischen Arbeiten Danzis / Volkmar von Pechstaedt. - Tutzing : Schneider, 1996. - XXXVII, 203 S., Noten. - 31 cm\nISBN 3-7952-0840-8\nIDN 946906769",
				"DanziWV", "118956124", "XA-DE" },
			{
				"Debussy, Claude",
				"L",
				"Lesure, François: Catalogue de l'oeuvre de Claude Debussy / François Lesure. - Genève : Minkoff, 1977. - 167 S. - 22 cm\nISBN 2-8266-0657-3\nIDN 974788651",
				"DebussyWV", "118524186", "XA-FR" },

			{
				"Diabelli, Anton",
				"WAD",
				"Kantner, Leopold M.: Anton Diabelli: thematisch-systematisches Werkverzeichnis (WAD). - München : Strube, 2006. - (Veröffentlichungen zur Salzburger Musikgeschichte ; Bd. 7)",
				"DiabelliWV", "119216132", "XA-AT;XA-DE" },
			{
				"Dittersdorf, Karl Ditters von",
				"K",
				"Krebs, Carl: Dittersdorfiana / by Carl Krebs. - Berlin : Paetel, 1900. - Nachdr. d. Ausg. Berlin 1900. - 182 S., Noten. - 23 cm\nISBN 0-306-70259-2\nIDN 974787027",
				"DittersdorfK", "118679856", "XA-AT" },
			{
				"Dittersdorf, Karl Ditters von",
				"G",
				"Grave, Margaret H.: Carl Ditters von Dittersdorf 1739 - 1799 In: The symphony 1720 - 1840 / comp. by Margaret H. Grave. - Barry S. Brook ed. - Reference vol. - New York [u.a.] : Garland, 1986. - S. 166 - 193\n[27] Bl., Noten. - 31 cm\nIDN 975609823",
				"DittersdorfG", "118679856", "XA-AT" },
			{
				"Dittersdorf, Karl Ditters von",
				"L",
				"Lane, Jay Donald: The concertos of Carl Ditters von Dittersdorf / Jay Donald Lane. - Ann Arbor, Mich., 1997. - [581] Bl., Ill., Noten. - 30 cm\nZugl.: Newhaven, Yale Univ., Diss. 1997\nIDN 979471680\nErl.: G bzw. L werden verwendet, wenn das Werk bei Krebs nicht nachgewiesen ist.",
				"DittersdorfL", "118679856", "XA-AT" },
			{
				"Dussek, Johann Ladislaus",
				"C",
				"Craw, Howard Allen: A biography and thematic catalog of the works of J. L. Dussek : (1760 - 1812) / by Howard Allen Craw. - Diss., Univ. of Southern California, 1964 . - 488 S. - 18 cm\nIDN 97476731X",
				"DussekWV", "11867286X", "XA-DE" },
			// ????
			//			{
			//				"Dvoÿérák, Antonín",
			//				"B",
			//				"Burghauser, Jarmil: Antonín Dvořák : thematick katalog / Jarmil Burghauser. Bibliografie: Jarmil Burghauser ; John Clapham. Übersicht d. Lebens u. d. Werkes: Jarmil Burghauser. - Praha : Bärenreiter Ed. Supraphon, 1996. - 843 S. - Faks. - 30 cm\nISBN 80-7058-410-6\nIDN 97476373X",
			//				"DvorakWV", "11852836X", "XA-CZ;XA-AT;XD-US" },
			{
				"Dvořák, Antonín",
				"B",
				"Burghauser, Jarmil: Antonín Dvořák : thematick katalog / Jarmil Burghauser. Bibliografie: Jarmil Burghauser ; John Clapham. Übersicht d. Lebens u. d. Werkes: Jarmil Burghauser. - Praha : Bärenreiter Ed. Supraphon, 1996. - 843 S. - Faks. - 30 cm\nISBN 80-7058-410-6\nIDN 97476373X",
				"DvorakWV", "11852836X", "XA-CZ;XA-AT;XD-US" },
			{
				"Eybler, Joseph von",
				"H",
				"Herrmann, Hildegard: Thematisches Verzeichnis der Werke von Joseph Eybler. - München [u.a.] : Katzbichler, 1976",
				"EyblerWV", "118531522", "XA-AT" },
			{
				"Fasch, Johann Friedrich",
				"FWV",
				"Pfeiffer, Rüdiger: Verzeichnis der Werke von Johann Friedrich Fasch : (FWV) / Rüdiger Pfeiffer. Hrsg. vom Zentrum für Telemann-Pflege und -Forschung Magdeburg. - Kleine Ausg.. - Magdeburg, 1988. - 104 S. - 21 cm\nIDN 943215315",
				"FWV", "11872262X", "XA-DE" },
			{
				"Fiala, Joseph",
				"R",
				"Reinländer, Claus: Joseph Fiala : thematisch-systematisches Werkverzeichnis / zsgest. von Claus Reinländer. - 2. erw. Aufl. - Puchheim : Engel, 1997. - 152 S., Noten. - 21 cm\nIDN 952082233",
				"FialaWV", "119135094", "XA-CZ;XA-DE" },

			//			{
			//				"Fibich, Zdenÿéek",
			//				"H",
			//				"Hudec, Vladimír: Zdeněk Fibich : tematický katalog. - Praha : Ed. Bärenreiter, 2001",
			//				"FibichWV", "117749443", "XA-CZ;XA-DE;XA-FR" },
			{
				"Fibich, Zdeněk",
				"H",
				"Hudec, Vladimír: Zdeněk Fibich : tematický katalog. - Praha : Ed. Bärenreiter, 2001",
				"FibichWV", "117749443", "XA-CZ;XA-DE;XA-FR" },
			{
				"Field, John",
				"H",
				"Hopkinson, Cecil: A bibliographical thematic catalogue of the works of John Field : 1782 - 1837 / by Cecil Hopkinson. - London : Harding and Curtis. - 175 S. - 28 cm\nIDN 974781460",
				"FieldWV", "118683578", "XA-IE;XA-RU" },
			{
				"Franck, César",
				"FWV",
				"Mohr, Wilhelm: Caesar Franck : 1782 - 1837 / Wilhelm Mohr. - 2., erg. Aufl. - Tutzing : Schneider, 1969. - 345 S., 1 Titelbild\nIDN 457627638",
				"FranckWV", "118534645", "XA-BE;XA-FR;XA-DE" },
			{ // alt
				"Franck, César",
				"M",
				"Mohr, Wilhelm: Caesar Franck : 1782 - 1837 / Wilhelm Mohr. - 2., erg. Aufl. - Tutzing : Schneider, 1969. - 345 S., 1 Titelbild\nIDN 457627638",
				"FranckWV", "118534645", "XA-BE;XA-FR;XA-DE" },
			{
				"Fux, Johann Joseph",
				"K",
				"Köchel, Ludwig von: Johann Josef Fux : Hofcompositor u. Hofkapellmeister d. Kaiser Leopold I., Josef I. u. Karl VI. von 1698 bis 1740 / Ludwig Ritter von Köchel. - Nachdr. der Ausg. Wien 1872. - Hildesheim [u.a.] : Olms, 1974. - XIV, 584, 187 S., 1 Ill., Noten. - 19 cm\nISBN 3-487-04483-8\nIDN 740364480",
				"FuxWV", "118694375", "XA-AT" },
			{
				"Gade, Niels Wilhelm",
				"op./WoO",
				"Fog, Dan: N. W. Gade-Katalog : en fortegnelse over Niels W. Gades trykte kompositioner : Verzeichnis der im Druck erschienenen Kompositionen von Niels W. Gade / Dan Fog. - Köbenhavn : Fog, 1986. - 96 S. - 21 cm. - Text dt. und dän.\nISBN 87-87099-29-2\nIDN 974773018",
				"GadeWV", "119057077", "XA-DK" },
			{
				"Genzmer, Harald",
				"GeWV",
				"Harald Genzmer, Werkverzeichnis / hrsg. von Markus Faul. - Mainz [u.a.] : Schott, 2011",
				"GeWV", "118538500", "XA-DE" },
			{
				"Giuliani, Mauro",
				"op.",
				"Heck, Thomas FitzSimons: The birth of the classic guitar and its cultivation in Vienna, reflected in the career and compositions of Mauro Giuliani. - Diss.,Yale Univ. - 286, XIV, 218 S. - 21 cm\nYale University, Phil. Diss., 1970\nIDN 974779334\nVol. 1. - 1971\nVol 2. Thematic catalogue of the complete works of Mauro Giuliani. - 1970",
				"GiulianiWV", "119380765", "XA-IT" },
			{
				"Gluck, Christoph Willibald von",
				"W",
				"Wotquenne, Alfred: Catalogue thématique des Oeuvres de Chr. W. v. Gluck / Alfred Wotquenne. - Reprogr. Nachdr. der Ausg. Leipzig 1904. - Hildesheim [u.a.] : Olms, 1967. - XI, 246 S., 1 Titelbild\nIDN 458701963",
				"GluckWV", "118539841", "XA-DE;XA-AT;XA-CZ;XA-IT;XA-GB" },

			{
				"Graun, Johann Gottlieb",
				"GraunWV",
				"Henzel, Christoph: Graun-Werkverzeichnis (GraunWV).- Beeskow : Ortus-Musikverl., 2006/n1. Verzeichnis der Werke der Brüder Johann Gottlieb und Carl Heinrich. - 2. Register",
				"GraunWV", "118541692", "XA-DE" },
			{
				"Graun, Carl Heinrich",
				"GraunWV",
				"Henzel, Christoph: Graun-Werkverzeichnis (GraunWV).- Beeskow : Ortus-Musikverl., 2006/n1. Verzeichnis der Werke der Brüder Johann Gottlieb und Carl Heinrich. - 2. Register",
				"GraunWV", "118541706", "XA-DE" },

			{
				"Graupner, Christoph",
				"GWV",
				"Christoph Graupner : thematisches Verzeichnis der musikalischen Werke ; Graupner-Werke-Verzeichnis GWV - Instrumentalwerke / hrsg. von Oswald Bill und Christoph Grosspietsch. - Stuttgart : Carus, 2005",
				"GWV", "118718517", "XA-DE" },

			{
				"Grieg, Edvard",
				"op./EG",
				"Grieg, Edvard: Samlede verker = Gesamtausgabe. - Frankfurt [u.a.] : Peters 20. Addenda, Corrigenda, Werkverzeichnisse und Register / Hrsg. Rune J. Andersen ... - 1995\nMehrb.Werk, Notendruck",
				"GriegWV", "118697641", "XA-NO" },
			{
				"Gruber, Franz Xaver",
				"H",
				"Hochradner, Thomas: Franz Xaver Gruber : thematisch-systematisches Verzeichnis der musikalischen Werke / vorgelegt von Thomas Hochradner. Im Auftr. der Stille-Nacht-Gesellschaft. [Red.: Ernst Hintermaier und Gerhard Walterskirchen]. - Bad Reichenhall : Comes-Verl., 1989. - IX, 161 S., Ill., Noten. - 27 cm\n(Veröffentlichungen zur Salzburger Musikgeschichte ; 1)\nISBN 3-88820-005-9\nIDN 920972829",
				"GruberWV", "11854277X", "XA-AT" },
			{
				"Händel, Georg Friedrich",
				"HWV",
				"Händel-Handbuch / hrsg. von d. Georg-Friedrich-Händel-Gesellschaft. - Kassel [u.a.] : Bärenreiter\nIDN 011248513\n1. 1978\n2. 1984\n3. 1986\n4. 1985",
				"HWV", "118544489", "XA-DE;XA-GB" },
			{
				"Haydn, Joseph",
				"Hob",
				"Hoboken, Anthony van: Joseph Haydn : thematisch-bibliographisches Werkverzeichnis / zsgest. von Anthony van Hoboken. - Mainz : Schott. - 27 cm\nIDN 550362703\n1 (1957) - 3 (1978)",
				"Hob", "118547356", "XA-AT" },

			{ // alt
				"Haydn, Joseph",
				"H",
				"Hoboken, Anthony van: Joseph Haydn : thematisch-bibliographisches Werkverzeichnis / zsgest. von Anthony van Hoboken. - Mainz : Schott. - 27 cm\nIDN 550362703\n1 (1957) - 3 (1978)",
				"Hob", "118547356", "XA-AT" },
			{
				"Haydn, Michael",
				"S",
				"Sherman, Charles H.: Johann Michael Haydn : (1737 - 1806) ; a chronological thematic catalogue of his works / by Charles H. Sherman and T. Donley Thomas. - Stuyvesant, NY : Pendragon Press, 1993. - XIV, 385 S., Ill., Noten. - 32 cm\n(Thematic catalogues ; 17)\nISBN 0-918728-56-8\nIDN 941403408",
				"MHaydnWV", "118639528", "XA-AT" },
			// alt
			{
				"Haydn, Michael",
				"P",
				"Haydn, Michael: Instrumentalwerke / Michael Haydn. Bearb. v. Lothar Herbert Perger. - Unveränd. Abdr. d. 1907 in Wien ersch. Ausg. Photomech. Nachdr. - Graz : Akademische Druck- u. Verl. Anst. ; Wiesbaden : Breitkopf ÿ& Härtel, 1959. - XXIX, 124 S. : Noten mit 1 Abb. ; 4  (Denkmäler der Tonkunst in Österreich ; Jg. 14. 2 = Bd. 29) S 340.-Signatur(en): ZB 62237 - 29",
				"???", "118639528", "XA-AT" },
			{
				"Hensel, Fanny",
				"H",
				"Hellwig-Unruh, Renate: Fanny Hensel, geb. Mendelssohn Bartholdy : thematisches Verzeichnis der Kompositionen / Renate Hellwig-Unruh. - Adliswil/ZH [u.a.] : Kunzelmann, 2000. - 452 S., Ill., Noten. - 22 cm\nZugl.: Berlin, Techn. Univ., Diss., 1999\nISBN 3-9521049-3-0\nIDN 960474161",
				"HenselWV", "118580736", "XA-DE" },
			{
				"Hoffmann, Ernst T. A.",
				"A",
				"Allroggen, Gerhard: E. T. A. Hoffmanns Kompositionen : ein chronologisch-thematisches Verzeichnis seiner musikal. Werke mit e. Einf. / Gerhard Allroggen. - Regensburg : Bosse, 1970. - 95, 143 S., Noten\nZUgl.: Diss., Univ. Hamburg., Fachbereich Philosophie, Psychologie, Sozialwiss., 1970\n(Studien zur Musikgeschichte des 19. Jahrhunderts ; 16)\nIDN 454562535",
				"ETAHoffmannWV", "118552465", "XA-DE;XA-RU" },
			{
				"Holst, Gustav",
				"H",
				"Holst, Imogen: A thematic catalogue of Gustav Holst's music / Imogen Holst. - London : Faber, 1974. - XXVIII, 285 S., Faks., zahlr. Noten. - 25 cm\nISBN 0-571-10004-X\nIDN 974781304",
				"HolstWV", "118774794", "XA-GB" },
			{
				"Homilius, Gottfried August",
				"HoWV",
				"Wolf, Uwe: Gottfried August Homilius (1714 - 1785) : thematisches Verzeichnis der musikalischen Werke (HoWV) / vorgelegt von Uwe Wolf. - Stuttgart : Carus-Verl., 2014. - (Ausgewählte Werke / Gottfried August Homilius ; Reihe 5 : Supplement ; Bd. 2).",
				"HoWV", "118553356", "XA-DE" },
			{
				"Hummel, Johann Nepomuk",
				"op./WoO",
				"Zimmerschied, Dieter: Thematisches Verzeichnis der Werke von Johann Nepomuk Hummel / von Dieter Zimmerschied. - Hofheim am Taunus : Hofmeister, 1971. - 207 S., zahlr. Noten. - 24 cm\nISBN 3-87350-000-0\nIDN 730068013",
				"HummelWV", "118554751", "XA-AT;XA-DE" },
			{
				"Humperdinck, Engelbert",
				"EHWV",
				"Humperdinck, Eva: Der unbekannte Engelbert Humperdinck : seine Werke ; Engelbert-Humperdinck-Werkverzeichnis (EHWV) ; [zum 140. Geburtstag] / Eva Humperdinck. - Koblenz : Görres, 1994. - 255 S., Ill. - 24 cm\nISBN 3-920388-38-0\nIDN 974782041",
				"HumperdinckWV", "11855476X", "XA-DE" },

			{
				"Humperdinck, Engelbert",
				"Ir",
				"Irmen, Hans-Josef: Thematisch-systematisches Verzeichnis der musikalischen Werke Engelbert Humperdincks. - Zülpich : Prisca-Verl., 2005",
				"Humperdinck Irmen", "11855476X", "XA-DE" },

			//			{
			//				"Janáÿécek, Leoÿés",
			//				"JW",
			//				"Simeone, Nigel: Janáček’s works : a catalogue of the music and writings of Leoš Janáček / Nigel Simeone ; John Tyrrell ; Alena Němcová. - Oxford : Clarendon Press, 1997",
			//				"JanáčekWV", "118556878", "XA-CZ" },
			{
				"Janáček, Leoš",
				"JW",
				"Simeone, Nigel: Janáček’s works : a catalogue of the music and writings of Leoš Janáček / Nigel Simeone ; John Tyrrell ; Alena Němcová. - Oxford : Clarendon Press, 1997",
				"JanáčekWV", "118556878", "XA-CZ" },

			{
				"Kalliwoda, Johann Wenzel",
				"op./WoO",
				"Strauß-Nemeth, László: Johann Wenzel Kalliwoda und die Musik am Hof von Donaueschingen. - Hildesheim [u.a.] : Olms 2. Vollständiges Werkverzeichnis. - 2005. - (Studien und Materialien zur Musikwissenschaft ; Bd. 38, 2)",
				"KalliwodaWV", "116032529", "XA-DE;XA-CZ" },
			{
				"Karg-Elert, Sigfrid",
				"op./WoO",
				"Gerlach, Sonja: Sigfrid Karg-Elert : Verzeichnis sämtlicher Werke / im Auftr. d. Karg-Elert-Archivs zsgest. von Sonja Gerlach.\n4010 {Biographie / verf. von Ralf Kaupenjohann}. - Frankfurt : Zimmermann, 1984. - XXV, 207 S., Ill. - 21 cm\nISBN 3-921729-23-8\nIDN 850702267",
				"Karg-ElertWV", "118835351", "XA-DE" },
			{
				"Koželuch, Leopold",
				"P",
				"Poštolka, Milan: Leopold Koželuh : život a dílo / Milan Poštolka. - 1. vyd. - Praha : Státní Hudební Vydavatelství, 1964. - 387 S. : Ill., Noten ; 24 cm\nIDN 974252859",
				"KoželuchWV", "11635190X", "XA-CZ" },
			//			{
			//				"Koÿézeluch, Leopold",
			//				"P",
			//				"Poštolka, Milan: Leopold Koželuh : život a dílo / Milan Poštolka. - 1. vyd. - Praha : Státní Hudební Vydavatelství, 1964. - 387 S. : Ill., Noten ; 24 cm\nIDN 974252859",
			//				"KoželuchWV", "11635190X", "XA-CZ" },
			{
				"Kraus, Joseph Martin",
				"VB",
				"Boer, Bertil H. van <junior>: Joseph Martin Kraus : (1756 - 1792) ; a systematic-thematic catalogue of his musical works and source study / by Bertil H. van Boer jr. Stuyvesant, NY : Pendragon Press, 1998. - XXX, 342 S. - 28 cm\n(Thematic catalogues series ; no. 26)\nISBN 0-945193-69-6\nIDN 974762075",
				"VB", "11856627X", "XA-DE;XA-SE" },
			{ // alt
				"Kraus, Joseph Martin",
				"B",
				"Boer, Bertil H. van <junior>: Joseph Martin Kraus : (1756 - 1792) ; a systematic-thematic catalogue of his musical works and source study / by Bertil H. van Boer jr. Stuyvesant, NY : Pendragon Press, 1998. - XXX, 342 S. - 28 cm\n(Thematic catalogues series ; no. 26)\nISBN 0-945193-69-6\nIDN 974762075",
				"VB", "11856627X", "XA-DE;XA-SE" },
			{
				"Kreutzer, Conradin",
				"KWV",
				"Brecht, Karl-Peter: Conradin Kreutzer : Biographie und Werkverzeichnis / Karl-Peter Brecht. Mit e. Einf. von Eberhard Stiefel. - Messkirch : Verl. der Stadt, 1980. - 306 S., Ill. - 21 cm\nIDN 820142468",
				"KWV", "11871600X", "XA-DE;XA-AT" },
			{
				"Krebs, Johann Ludwig",
				"KrebsWV",
				"Friedrich,  Felix:  Krebs-Werkeverzeichnis  (Krebs-WV)  :thematisch-systematisches Verzeichnis der musikalischen Werke  von  Johann  Ludwig  Krebs  /  Felix  Friedrich.  - Altenburg : Kamprad, 2009",
				"KrebsWV", "123281954", "XA-DE" },
			{ // alt
				"Kreutzer, Conradin",
				"B",
				"Brecht, Karl-Peter: Conradin Kreutzer : Biographie und Werkverzeichnis / Karl-Peter Brecht. Mit e. Einf. von Eberhard Stiefel. - Messkirch : Verl. der Stadt, 1980. - 306 S., Ill. - 21 cm\nIDN 820142468",
				"KWV", "11871600X", "XA-DE;XA-AT" },
			{
				"Küffner, Joseph (Komponist)",
				"op./WoO",
				"Henke, Matthias: Joseph Küffner : Leben und Werk des Würzburger Musikers im Spiegel der Geschichte / Matthias Henke. - Tutzing : Schneider. - 24 cm\nZugl.: Münster (Westfalen), Univ., Diss., 1983\nIDN 551060123\n2. Thematisch-bibliographisches Verzeichnis der Werke Joseph Küffners. - 1985",
				"KüffnerWV", "123431891", "XA-DE" },
			{
				"Kuhlau, Friedrich",
				"op./WoO",
				"Fog, Dan: Kompositionen von Fridr. Kuhlau : thematisch-bibliographischer Katalog / Dan Fog. - Kopenhagen : Fog, 1977. - 203 S., Noten. - 21 cm\nISBN 87-87099-09-8\nIDN 790241250",
				"KuhlauWV", "118567837", "XA-DE;XA-DK" },
			{
				"Lanner, Joseph",
				"op./WV Anh.",
				"Dörner, Wolfgang: Joseph Lanner : chronologisch-thematisches Werkverzeichnis / Wolfgang Dörner. - Wien [u.a.] : Böhlau, 2012.",
				"LannerWV", "118569597", "XA-AT" },
			{
				"Liszt, Franz",
				"R",
				"Raabe, Peter: Franz Liszt / Peter Raabe. - Nachdr. - Tutzing : Schneider\nIDN 457870095\nBd. 2. Liszts Schaffen, 1968. - 2., erg. Aufl. - 379, 46 S. : Notenbeisp.\nIDN 457870117",
				"LisztWV", "118573527", "XA-HU;XA-DE" },
			{
				"Locatelli, Pietro Antonio",
				"op.",
				"Locatelli, Pietro Antonio: Opera omnia / Pietro Antonio Locatelli. Ed. critica dir. da Albert Dunning. - London [u.a.] : Schott. - 31 cm\nIDN 974286508\n10. Catalogo tematico, lettere, documenti & iconografia / a cura di Albert Dunning. - 2001",
				"LocatelliWV", "118780220", "XA-IT" },
			{
				"Lortzing, Albert",
				"LoWV",
				"Capelle, Irmlind: Chronologisch-thematisches Verzeichnis der Werke von Gustav Albert Lortzing : (LoWV) / bearb. von Irmlind Capelle. - Köln : Studio, 1994. - VI, 459 S., Noten. - 24 cm\nZugl.: Paderborn, Univ., Diss., 1991\nISBN 3-89564-003-4\nIDN 94148548X",
				"LortzingWV", "118574469", "XA-DE" },
			{
				"Lully, Jean-Baptiste",
				"LWV",
				"Schneider, Herbert: Chronologisch-thematisches Verzeichnis sämtlicher Werke von Jean-Baptiste Lully : (LWV) / Herbert Schneider. - Tutzing : Schneider, 1981. - 570 S., zahlr. Notenbeispiele. - 31 cm\n(Mainzer Studien zur Musikwissenschaft ; 14)\nISBN 3-7952-0323-6\nIDN 820060895",
				"LullyWV", "118575287", "XA-FR" },
			{// alt
				"Lully, Jean-Baptiste",
				"S",
				"Schneider, Herbert: Chronologisch-thematisches Verzeichnis sämtlicher Werke von Jean-Baptiste Lully : (LWV) / Herbert Schneider. - Tutzing : Schneider, 1981. - 570 S., zahlr. Notenbeispiele. - 31 cm\n(Mainzer Studien zur Musikwissenschaft ; 14)\nISBN 3-7952-0323-6\nIDN 820060895",
				"LullyWV", "118575287", "XA-FR" },
			{
				"Marcello, Alessandro",
				"S",
				"Selfridge-Field, Eleanor: The music of Benedetto & Alessandro Marcello : a thematic catalogue, with commentary on the composers, repertory and sources / Eleanor Selfridge-Field. - Oxford : Clarendon. - XVII, 517 S. - 24 cm\nISBN 0-19-316126-5\nIDN 974805319",
				"MarcelloWV", "118951114", "XA-IT" },

			{
				"Marcello, Benedetto",
				"S",
				"Selfridge-Field, Eleanor: The music of Benedetto & Alessandro Marcello : a thematic catalogue, with commentary on the composers, repertory and sources / Eleanor Selfridge-Field. - Oxford : Clarendon. - XVII, 517 S. - 24 cm\nISBN 0-19-316126-5\nIDN 974805319",
				"MarcelloWV", "118951122", "XA-IT" },

			//			{
			//				"Martinÿêu, Bohuslav",
			//				"H",
			//				"Halbreich, Harry: Bohuslav Martinu : Werkverzeichnis, Dokumentation und Biographie / Harry Halbreich. - Zürich [u.a.] : Atlantis-Verl., 1968. - 384 S.\nIDN 456864032",
			//				"MartinuWV", "118578413", "XA-CZ;XD-US;XA-CH" },
			{
				"Martin°u, Bohuslav",
				"H",
				"Halbreich, Harry: Bohuslav Martinu : Werkverzeichnis, Dokumentation und Biographie / Harry Halbreich. - Zürich [u.a.] : Atlantis-Verl., 1968. - 384 S.\nIDN 456864032",
				"MartinuWV", "118578413", "XA-CZ;XD-US;XA-CH" },
			{
				"Mendelssohn Bartholdy, Felix",
				"MWV/op.",
				"Thematisches Verzeichnis der im Druck erschienenen Compositionen von Felix Mendelssohn Bartholdy. - Nachdr. der 3., vervollst. Ausg. Leipzig 1882. - London : Baron, 1966. - 99 S.\nIDN 576792187",
				"MendelssohnWV", "118580779", "XA-DE" },
			{
				"Molter, Johann Melchior",
				"MWV",
				"Häfner, Klaus: Der badische Hofkapellmeister Johann Melchior Molter (1696 - 1765) in seiner Zeit : Dokumente und Bilder zu Leben und Werk ; eine Ausstellung der Badischen Landesbibliothek Karlsruhe zum 300. Geburtstag des Komponisten / hrsg. von der Badischen Landesbibliothek Karlsruhe. Klaus Häfner. (Mit einem Beitr. von Rainer Fürst). - Karlsruhe : Bad. Landesbibl., 1996. - 408 S., Ill., Noten. - 24 cm\nISBN 3-88705-041-X\nIDN 946994404",
				"MolterWV", "119330563", "XA-DE" },

			{
				"Mozart, Leopold",
				"LMV",
				"Eisen, Cliff: Leopold-Mozart-Werkverzeichnis : (LMV) / Cliff Eisen, unter Mitarb. von Christian Broy. - Augsburg : Wißner, 2010. - (Beiträge zur Leopold-Mozart- Forschung ; Bd. 4)",
				"LMV", "118584588", "XA-DE;XA-AT" },

			{
				"Mozart, Wolfgang Amadeus",
				"KV",
				"Köchel, Ludwig von: Chronologisch-thematisches Verzeichnis sämtlicher Tonwerke Wolfgang Amadé Mozarts : nebst Angabe d. verlorengegangenen, angefangenen, von fremder Hand bearb., zweifelhaften u. unterschobenen Kompositionen / Ludwig Ritter von Köchel. - 6. Aufl. bearb. von Franz Giegling [u.a.]. - Wiesbaden : Breitkopf & Härtel, 1964. - CXLIII, 1024 S., 1 Titelbild, zahlr. Noten\nIDN 452488699",
				"KV", "118584596", "XA-AT" },
			{
				"Mysliveček, Josef",
				"ED",
				"Evans, Angela: Josef Myslivecek : (1737 - 1781) ; a thematic catalogue of his instrumental and orchestral works / Angela Evans ; Robert Dearling. - München [u.a.] : Katzbichler, 1999. - 188 S., Noten. - 24 cm\n(Musikwissenschaftliche Schriften ; 35)\nISBN 3-87397-132-1\nIDN 95993913X",
				"MyslivecekWV", "118735500", "XA-CZ;XA-IT" },
			//			{
			//				"Mysliveÿécek, Josef",
			//				"ED",
			//				"Evans, Angela: Josef Myslivecek : (1737 - 1781) ; a thematic catalogue of his instrumental and orchestral works / Angela Evans ; Robert Dearling. - München [u.a.] : Katzbichler, 1999. - 188 S., Noten. - 24 cm\n(Musikwissenschaftliche Schriften ; 35)\nISBN 3-87397-132-1\nIDN 95993913X",
			//				"MyslivecekWV", "118735500", "XA-CZ;XA-IT" },
			{
				"Nielsen, Carl",
				"FS",
				"Fog, Dan: Carl Nielsen kompositioner : en bibliograf / ved Dan Fog / i samarbejde med Torben Schousboe. - Köbenhavn : Nyt Nordisk Forlag, 1965. - 65 S. - 24 cm\nIDN 974772976",
				"NielsenWV", "118786180", "XA-DK" },
			{// alt
				"Nielsen, Carl",
				"F",
				"Fog, Dan: Carl Nielsen kompositioner : en bibliograf / ved Dan Fog / i samarbejde med Torben Schousboe. - Köbenhavn : Nyt Nordisk Forlag, 1965. - 65 S. - 24 cm\nIDN 974772976",
				"NielsenWV", "118786180", "XA-DK" },
			{
				"Pachelbel, Johann",
				"P",
				"Perreault,  Jean  M.:  The  thematic  catalogue  of  musical works  of  Johann  Pachelbel.  -  Lanham   [u.a.]  :  The Scarecrow Press, 2004",
				"PachelbelWV", "119456613", "XA-DE" },
			{
				"Paganini, Niccolò",
				"M.S.",
				"Moretti, Maria Rosa: Catalogo tematico delle musiche di Niccoló Paganini / a cura di Maria Rosa Moretti e Anna Sorrento. - Genova : Comune di Genova, 1982. - XXVI, 422 S. - 27 cm\nIDN 974769541",
				"PaganiniWV", "118591177", "XA-IT" },
			{// alt
				"Paganini, Niccolò",
				"M",
				"Moretti, Maria Rosa: Catalogo tematico delle musiche di Niccoló Paganini / a cura di Maria Rosa Moretti e Anna Sorrento. - Genova : Comune di Genova, 1982. - XXVI, 422 S. - 27 cm\nIDN 974769541",
				"PaganiniWV", "118591177", "XA-IT" },
			{
				"Paisiello, Giovanni",
				"R",
				"Robinson, Michael F.: Giovanni Paisiello : a thematic catalogue of his works / by Michael F. Robinson. Stuyvesant, NY : Pendragon Press. - 28 cm\n(Thematic catalogues series ; no. 15)\nIDN 560850344\nVol. 1. (1991)\nVol. 2. (1994) The non-dramatic works",
				"PaisielloWV", "119010097", "XA-IT" },
			{
				"Pfitzner, Hans",
				"op.",
				"Grohe, Helmut: Hans Pfitzner : Verzeichnis sämtlicher im Druck erschienenen Werke / Helmut Grohe. Im Auftr. d. Hans-Pfitzner-Gesellschaft zusammengest. - München [u.a.] : Leuckart, 1960. - 24 S.\nIDN 451677498",
				"PfitznerWV", "118593625", "XA-DE;XA-RU;XA-AT" },
			{
				"Pleyel, Ignaz",
				"B",
				"Benton, Rita: Ignace Pleyel : a thematic catalogue of his compositions / by Rita Benton. - New York : Pendragon Press, 1977. - XXVIII, 482 S. - Faks., Noten. - 29 cm\n(Thematic catalogues series ; No. 2)\nISBN 0-918728-04-5\nIDN 974760390",
				"PleyelWV", "118792555", "XA-AT;XA-FR" },
			{
				"Purcell, Henry",
				"Z",
				"Zimmerman, Franklin B.: Henry Purcell : 1659 - 1695 ; an analytical catalogue of his music / by Franklin B. Zimmerman. - London [u.a.] : Macmillan, 1963. - XXIV, 575 S. : zahlr. Notenbeisp.",
				"PurcellWV", "118742973", "XA-GB" },
			{
				"Quantz, Johann Joachim",
				"QV",
				"Augsbach, Horst: Thematisch-systematisches Werkverzeichnis (QV) Johann Joachim Quantz / Host Augsbach. - Stuttgart : Carus-Verl., 1997. - XXXIII, 333 S., Noten. - 30 cm\nISBN 3-923053-50-9\nIDN 949315842",
				"QuantzWV", "118747975", "XA-DE" },
			{
				"Rachmaninov, Sergej V.",
				"op./T",
				"Threlfall, Robert: A catalogue of the compositions of S. Rachmaninoff / by Robert Threlfall and Geoffrey Norris. - London : Scolar Press, 1982. - 218 S. - 25 cm\nISBN 0-85967-617-X\nIDN 974809721",
				"RachmaninovWV", "118641832", "XA-RU" },
			{
				"Raff, Joseph Joachim",
				"op.",
				"Schäfer, Albert: Chronologisch-systematisches Verzeichnis der Werke Joachim Raffs mit Einschluss der verloren gegangenen, unveröffentlichten und nachgelassenen Kompositionen dieses Meisters : unter genauer Angabe d. Beschaffenheit, der Umarbeitungen u. Übertr. bearb., sowie mit histor. Anm. versehen / von Albert Schäfer. - Unveränd. Nachdr. der Ausg. von 1888. - Tutzing : Schneider, 1974. - 164 S. - 22 cm\nISBN 3-7952-0134-9\nIDN 740364839",
				"RaffWV", "118597779", "XA-DE;XA-CH" },
			{
				"Rameau, Jean-Philippe",
				"RCT",
				"Bouissou,   Sylvie:   Jean-Philippe   Rameau   :   catalogue thématique  des  oeuvres  musicales  /  Sylvie  Bouissou  et Denis Herlin ... - Paris : CNRS Ed., [u.a.]. - (Collection Sciences de la musique : série références) T. 1. Musique instrumentale, musique vocale religieuse et profane. - 2007 T. 2. Livrets. - 2003",
				"RCT", "118598090", "XA-FR" },
			{
				"Reger, Max",
				"op./WoO",
				"Stein, Fritz: Thematisches Verzeichnis der im Druck erschienenen Werke von Max Reger einschlieälich seiner Bearbeitungen und Ausgaben : Mit systemat. Verz. u. Reg. / Bearb. Fritz Stein. Bibliographie d. Reger-Schrifttums v. Josef Bachmair. - Leipzig : Breitkopf & Härtel, 1953. - VIII, 617 S., 1 Titelb.\nIDN 577466674",
				"RegerWV", "118598988", "XA-DE" },
			{
				"Rheinberger, Joseph",
				"op./WoO/JWV",
				"Irmen, Hans-Josef: Thematisches Verzeichnis der musikalischen Werke Gabriel Josef Rheinbergers / von Hans-Josef Irmen. - Regensburg : Bosse, 1974. - 592 S., Noten. - 25 cm\n(Studien zur Musikgeschichte des 19. Jahrhunderts ; Bd. 37)\nISBN 3-7649-2080-7\nIDN 750103272",
				"RheinbergerWV", "118744828", "XA-DE" },
			{
				"Ries, Ferdinand",
				"op./WoO",
				"Hill, Cecil: Ferdinand Ries : a thematic catalogue / Cecil Hill. - Armidale : Univ. of New England, 1977. - XX, 260 S. - 23 cm\n(University of New England monographs ; 1)\nISBN 0-85834-156-5\nIDN 974780332",
				"RiesWV", "118600966", "XA-DE" },
			{
				"Rosetti, Antonio",
				"M",
				"Murray, Sterling E.: The music of Antonio Rosetti (Anton Rösler) : ca. 1750 - 1792 ; a thematic catalogue / by Sterling E. Murray. - Warren, Mich. : Harmonie Park Press, 1996. - LVIII, 861 S. - 28 cm\n(Detroit studies in music bibliography ; no. 76)\nISBN 0-89990-105-0\nIDN 974793388",
				"RöslerWV", "119118602", "XA-CZ;XA-DE" },
			// abweichende Form wg. Namensänderung
			{
				"Rösler, Franz Anton",
				"M",
				"Murray, Sterling E.: The music of Antonio Rosetti (Anton Rösler) : ca. 1750 - 1792 ; a thematic catalogue / by Sterling E. Murray. - Warren, Mich. : Harmonie Park Press, 1996. - LVIII, 861 S. - 28 cm\n(Detroit studies in music bibliography ; no. 76)\nISBN 0-89990-105-0\nIDN 974793388",
				"RöslerWV", "119118602", "XA-CZ;XA-DE" },
			// alt
			{
				"Rosetti, Antonio",
				"K",
				"Kaul, Oskar: Thematisches Verzeichnis der Instrumentalwerke von Anton Rosetti : Mit Angabe d. Druckausg. u. d. Fundorte erhaltener Ex. in Druck u. Hs. / Oskar Kaul. - Wiesbaden : Breitkopf ÿ& Härtel, 1968. - 27 S. ; 4  9.-Signatur(en): 1968 B 1851, Sbm 800 Rösler, A 0/kaul, HBD ; Sbm 800 Rösler, A 0/kaul",
				"???", "119118602", "XA-CZ;XA-DE" },
			{
				"Rösler, Franz Anton",
				"K",
				"Kaul, Oskar: Thematisches Verzeichnis der Instrumentalwerke von Anton Rosetti : Mit Angabe d. Druckausg. u. d. Fundorte erhaltener Ex. in Druck u. Hs. / Oskar Kaul. - Wiesbaden : Breitkopf ÿ& Härtel, 1968. - 27 S. ; 4  9.-Signatur(en): 1968 B 1851, Sbm 800 Rösler, A 0/kaul, HBD ; Sbm 800 Rösler, A 0/kaul",
				"???", "119118602", "XA-CZ;XA-DE" },

			{
				"Roussel, Albert",
				"L",
				"Labelle, Nicole: Catalogue raisonné de l'Oeuvre d'Albert Roussel / par Nicole Labelle. - Louvain-la-Neuve : Dép. d'Archéologie et d'Histoire de l'Art, Collége Erasme, 1992. - XII, 159 S., Ill., Noten. - 32 cm\nIDN 975602969",
				"RousselWV", "118750011", "XA-FR" },
			{
				"Saint-Saëns, Camille",
				"R",
				"Ratner, Sabina Teller: Camille Saint-Saëns : 1835 - 1921 ; a thematic catalogue of his complete works. - Oxford [u.a.] : Oxford Univ. Press. - 26 cm\nIDN 977472736\n1. The instrumental works. - 2002 -",
				"SaintSaensWV", "11875081X", "XA-FR;XC-DZ" },
			{
				"Scarlatti, Domenico",
				"K",
				"Kirkpatrick, Ralph: Domenico Scarlatti / Ralph Kirkpatrick. [Das Werk wurde vom Verf. f. d. dt. Erstausg. erw. u. von Horst Leuchtmann aus d. Amerikan. Übertr.]. - München : Ellermann. - 26 cm\nISBN 3-7707-7630-5\nIDN 540079146\nBd. 2. Anhang, Dokumente und Werkverzeichnis. - 1972",
				"DScarlattiWV", "118804952", "XA-IT" },
			{
				"Schaffrath, Christoph",
				"CSWV",
				"Oestreich, Reinhard: Verzeichnis der Werke Christoph Schaffraths : (CSWV). - Beeskow : Ortus-Musikverlag, 2012. - (Ortus-Studien ; 7)",
				"CSWV", "129014893", "XA-DE" },

			{
				"Scheidt, Samuel",
				"SSWV",
				"Samuel-Scheidt-Werke-Verzeichnis : (SSWV) / hrsg. von Klaus-Peter Koch. - Wiesbaden [u.a.] : Breitkopf & Härtel, 2000",
				"SSWV", "118754394", "XA-DE" },

			{
				"Schoeck, Othmar",
				"op./o.op.",
				"Vogel, Werner: Thematisches Verzeichnis der Werke von Othmar Schoeck / Werner Vogel. - Zürich : Atlantis-Verl., 1956. - 297 S. - 1 Titelbild\nIDN 455281955",
				"SchoeckWV", "118609920", "XA-CH" },

			{
				"Schönberg, Arnold",
				"op.",
				"Rufer, Josef: Das Werk Arnold Schönbergs / Josef Rufer. - Kassel [u.a.] : Bärenreiter, 1959. - XII, 207 S., mit 10 Bildern u. 25 Hss.-Faks.\nIDN 454228929",
				"SchönbergWV", "118610023", "XA-AT;XA-DE;XD-US" },
			{
				"Schubert, Franz",
				"D",
				"Schubert, Franz: Neue Ausgabe sämtlicher Werke / Franz Schubert. Hrsg. von der Internat. Schubert-Ges. - Kassel [u.a.] : Bärenreiter, 1978\nIDN 978010906\nSerie 8. Supplement\nBd. 4.*Thematisches Verzeichnis seiner Werke in chronologischer Folge / von Otto Erich Deutsch. - Neuausg. in dt. Sprache",
				"D", "118610961", "XA-AT" },
			{
				"Schütz, Heinrich",
				"SWV",
				"Bittinger, Werner: Schütz-Werke-Verzeichnis : (SWV) / Werner Bittinger. Im Auftr. d. Neuen Schütz-Ges. hrsg. - Kleine Ausg. - Kassel [u.a.] : Bärenreiter, 1960. - XXXI, 191 S.\nIDN 450484173",
				"SWV", "11861116X", "XA-DE" },
			{
				"Schumann, Clara",
				"op./WoO",
				"Koch, Paul-August: Clara Wieck-Schumann : (1819 - 1896) ; Kompositionen / eine Zusammenstellung der Werke, Literatur und Schallplatten / von Paul-August Koch. - Frankfurt am Main : Zimmermann, 1991. - 48 S., zahlr. Noten, graph. Darst. - 21 cm\nISBN 3-921729-51-3\nIDN 974786136",
				"CSchumannWV", "11861164X", "XA-DE" },
			{
				"Schumann, Robert",
				"op./WoO/A-R/M",
				"McCorkle, Margit L.: Thematisch.bibliographisches Werkverzeichnis / von Margit L. McCorkle. Unter Mitw. von Akio Mayeda und der Robert-Schumann-Forschungsstelle. Hrsg. von der Robert-Schumann-Gesellschaft, Düsseldorf. - Mainz [u.a.] : Schott, 2003. - 86, 1044 S., Ill., Noten. - 27 cm\n(Neue Ausgabe sämtlicher Werke : Ser. VIII, Suppl. ; Bd. 6 / Robert Schumann)\nISMN M-001-13362-3\nIDN 979385105",
				"RSchumannWV", "118611666", "XA-DE" },
			{
				"Schwarz-Schilling, Reinhard",
				"H",
				"Heller, Margot: Reinhard Schwarz-Schilling : Werkverzeichnis und Schriften / Margot Heller. - Wolfenbüttel [u.a.] : Möseler, 1986. - 191 S., Ill. (z.T. farb.), zahlr. Notenbeispiele. - 24 cm\nIDN 861202880",
				"SchwarzSchillingWV", "118812718", "XA-DE" },
			{
				"Sibelius, Jean",
				"op./JS",
				"Dahlström, Fabian: Jean Sibelius : thematisch-bibliographisches Verzeichnis seiner Werke / von Fabian Dahlström. - Wiesbaden [u.a.] : Breitkopf & Härtel, 2003. - XLVII, 768 S., Ill., zahlr. Noten. - 28 cm\nISBN 3-7651-0333-0\nIDN 969505655",
				"SibeliusWV", "118642405", "XA-FI" },

			{
				"Skrjabin, Aleksandr N.",
				"op./WoO/B",
				"Bosshard, Daniel: Thematisch-chronologisches Verzeichnis der musikalischen Werke von Alexander Skrjabin. - Ardez : Ed. Trais Giats ; Planegg bei München : Thomi-Berg [Vertrieb], 2002",
				"SkrjabinWV", "118614916", "XA-RU" },
			//			{
			//				"ÿéSostakoviÿéc, Dmitrij D.",
			//				"op.",
			//				"Hulme, Derek C.: Dmitri Shostakovich : a catalogue, bibliography and discography / Derek C. Hulme.\n - 2. ed. - Oxford : Clarendon Press, 1991. - XIIi, 479 p. - 24 cm\nISBN 0198162049",
			//				"SostakovicWV", "118642472", "XA-RU" },
			{
				"Šostakovič, Dmitrij D.",
				"op.",
				"Hulme, Derek C.: Dmitri Shostakovich : a catalogue, bibliography and discography / Derek C. Hulme.\n - 2. ed. - Oxford : Clarendon Press, 1991. - XIIi, 479 p. - 24 cm\nISBN 0198162049",
				"SostakovicWV", "118642472", "XA-RU" },
			{
				"Sperger, Johannes Matthias",
				"M",
				"Meier, Adolf: Thematisches Werkverzeichnis der Kompositionen von Johannes Sperger : (1750 - 1812) / zsgest. u. dokumentiert von Adolf Meier. [Kultur- u. Forschungsstätte Michaelstein, Inst. für Aufführungspraxis. Hrsg. von Eitelfriedrich Thom]. Michaelstein/Blankenburg : Kultur- und Forschungsstätte Michaelstein, 1990. - 90 S., zahlr. Noten. - 30 cm\n(Kultur- und Forschungsstätte <Michaelstein>: Dokumentationen, Reprints ; 21)\nIDN 901010464",
				"SpergerWV", "118867326", "XA-AT" },
			{
				"Spohr, Louis",
				"op./WoO",
				"Göthel, Folker: Thematisch-bibliographisches Verzeichnis der Werke von Louis Spohr / Folker Göthel. - Tutzing : Schneider, 1981. - XXIV, 576 S., Ill., zahlr. Noten. - 30 cm\nISBN 3-7952-0175-6\nIDN 820985058",
				"SpohrWV", "118616366", "XA-DE" },
			{
				"Stolz, Robert",
				"op.",
				"Pflicht, Stephan: Robert Stolz - Werkverzeichnis / von Stephan Pflicht. Hrsg. im Auftr. d. Robert-Stolz-Stiftung e.V. aus Anlass d. 100. Geburtstages d. Komponisten. - München [u.a.] : Katzbichler [u.a.], 1981. - XVI, 551 S. - 25 cm\nISBN 3-87397-420-7\nIDN 881117218",
				"StolzWV", "118618652", "XA-AT;XA-FR;XD-US" },
			{
				"Strauss, Johann (Komponist, 1825-1899)",
				"op./SEV",
				"Strauß-Elementar-Verzeichnis : (SEV) ; thematisch-bibliographischer Katalog der Werke von Johann Strauss (Sohn) / hrsg. vom Wiener Institut für Strauss-Forschung. - Tutzing : Schneider. - 30 cm\n(Schriftenreihe zur Musik ; 6)\nIDN 551819200\nTeilbd. A, Lfg. 1. 1990 -",
				"JStaussWV", "11861908X", "XA-AT;XA-DE" },
			{
				"Strauss, Richard",
				"op./AV",
				"Müller von Asow, Erich Hermann: Richard Strauss : thematisches Verzeichnis / E. H. Mueller von Asow. - Wien [u.a.] : Doblinger. - 64 S., Noten\nIDN 577291874 \n1 (1955) - 3 (1974)",
				"RStraussWV", "11861911X", "XA-DE" },
			{
				"Stravinsky, Igor",
				"K",
				"Kirchmeyer, Helmut: Kommentiertes Verzeichnis der Werke und Werkausgaben Igor Strawinskys bis 1971 / Helmut Kirchmeyer. Sächsische Akademie der Wissenschaften zu Leipzig. - Stuttgart [u.a.] : Sächs. Akad. der Wiss., 2002. - 602 S. - 25 cm\n(Abhandlungen der Sächsischen Akademie der Wissenschaften zu Leipzig : Philologisch-historische Klasse ; 79)\nISBN 3-7776-1156-5\nIDN 963046403",
				"StravinskyWV", "118642545", "XA-RU;XD-US" },

			{
				"Süßmayr, Franz Xaver",
				"SmWV",
				"Duda, Erich: Das musikalische Werk Franz Xaver Süßmayrs : thematisches Werkverzeichnis (SmWV) mit ausführlichen Quellenangaben und Skizzen der Wasserzeichen / Erich Duda. - Kassel [u.a.] : Bärenreiter, 2000. - (Schriftenreihe der Internationalen Stiftung Mozarteum Salzburg ; Bd. 12)",
				"SmWV", "11861990X", "XA-AT" },
			{
				"Tartini, Giuseppe",
				"D",
				"Dounias, Minos: Die Violinkonzerte Giuseppe Tartinis als Ausdruck einer Persönlichkeit und einer Kulturepoche : Mit vielen Notenbeisp. u.e. themat. Verzeichnis / Minos Dounias. - Fotomechan. Nachdr. d. Ausg. Wolfenbüttel 1935. - Wolfenbüttel [u.a.] : Möseler, 1966. - VIII, 307 S.\nIDN 456483772",
				"TartiniD", "118620878", "XA-IT" },
			{
				"Tartini, Giuseppe",
				"B",
				"Brainard, Paul: Die Violinsonaten Giuseppe Tartinis. - Göttingen, 1959",
				"TartiniB", "118620878", "XA-IT" },
			{
				"Telemann, Georg Philipp",
				"TWV",
				"Menke, Werner: Thematisches Verzeichnis der Vokalwerke von Georg Philipp Telemann / Werner Menke. - Frankfurt am Main : Klostermann. - 28 cm\nIDN 550682775\nBd. 1. Cantaten zum gottesdienstlichen Gebrauch (1982)\nBd. 2. (1995) - 2. Aufl.\n\nRuhnke, Martin: Georg Philipp Telemann : thematisch-systematisches Verzeichnis seiner Werke ; Telemann-Werkverzeichnis (TWV) / hrsg. von Martin Ruhnke. - Kassel [u.a.] : Bärenreiter. - 27 cm\nIDN 560848188\nBd. 1. Instrumentalwerke. 1984 - \n(Musikalische Werke : Suppl. / Georg Philipp Telemann)",
				"TWV", "11862119X", "XA-DE" },
			{
				"Torelli, Giuseppe (Musiker)",
				"G",
				"Giegling, Franz: Giuseppe Torelli : ein Beitrag z. Entwicklungsgeschichte d. italienischen Konzerts / Franz Giegling. - Kassel [u.a.] : Bärenreiter, 1949. - 88, 36 S., [Mit Notenbeil.]\nIDN 451542029",
				"TorelliWV", "118802550", "XA-IT" },
			//			{
			//				"Vanhal, Jan Krtitel",
			//				"W",
			//				"Themen-Verzeichnis der Kompositionen von Johann Baptiste Wanhal / hrsg. von Alexander Weinmann. -Wien : Krenn, [1987]. - (Wiener Archivstudien ; Bd. 11)Teil 1.Teil 2.",
			//				"VanhalWV", "118855034", "XA-CZ;XA-AT" },
			//			{
			//				"Vaÿénhal, Jan Kÿértitel",
			//				"W",
			//				"Themen-Verzeichnis der Kompositionen von Johann Baptiste Wanhal / hrsg. von Alexander Weinmann. -Wien : Krenn, [1987]. - (Wiener Archivstudien ; Bd. 11)Teil 1.Teil 2.",
			//				"VanhalWV", "118855034", "XA-CZ;XA-AT" },
			{
				"Vaňhal, Jan Křtitel",
				"W",
				"Themen-Verzeichnis der Kompositionen von Johann Baptiste Wanhal / hrsg. von Alexander Weinmann. -Wien : Krenn, [1987]. - (Wiener Archivstudien ; Bd. 11)Teil 1.Teil 2.",
				"VanhalWV", "118855034", "XA-CZ;XA-AT" },

			{
				"Verdi, Giuseppe",
				"H",
				"Hopkinson, Cecil: A bibliography of the works of Giuseppe Verdi : 1813 - 1901 / by Cecil Hopkinson. - New York : Broude. - 28 cm\nIDN 560848625\nVol. 1. Vocal and instrumental works. - (1973)\nVol. 2. Operatic works. - (1978)",
				"VerdiWV", "118626523", "XA-IT" },
			{
				"Viotti, Giovanni Battista",
				"W",
				"White, Chappell: Giovanni Battista Viotti : (1755 - 1824) : a thematic catalogue of his works / by Chappell White. - New York : Pendragon Press, 1985. - XIX, 175 S., Ill. - 26 cm\nISBN 0-918728-43-6\n(Thematic catalogues ; 12)\nIDN 974813141",
				"ViottiWV", "119322366", "XA-IT" },
			{
				"Vivaldi, Antonio",
				"RV",
				"Ryom, Peter: Verzeichnis der Werke Antonio Vivaldis : (RV). - 2. verb. und erw. Aufl. - Leipzig : Dt. Verl. für Musik, 1974. - 226 S. : Notenbeisp.\n\n\nRyom, Peter: Répertoire des Oeuvres d'Antonio Vivaldi / Peter Ryom. - Copenhague : Engström & Södring, 1986. - 25 cm\nIDN 560850514",
				"RV", "118627287", "XA-IT" },
			{// alt
				"Vivaldi, Antonio",
				"R",
				"Ryom, Peter: Verzeichnis der Werke Antonio Vivaldis : (RV). - 2. verb. und erw. Aufl. - Leipzig : Dt. Verl. für Musik, 1974. - 226 S. : Notenbeisp.\n\n\nRyom, Peter: Rèpertoire des Oeuvres d'Antonio Vivaldi / Peter Ryom. - Copenhague : Engström & Södring, 1986. - 25 cm\nIDN 560850514",
				"RV", "118627287", "XA-IT" },
			{
				"Wagenseil, Georg Christoph",
				"WV",
				"Scholz-Michelitsch, Helga: Das Orchester- und Kammermusikwerk von Georg Christoph Wagenseil : thematischer Katalog / Helga Scholz-Michelitsch. - Wien [u.a.] : Bählau, 1972. - 228 S., Überwiegend Noten. - 24 cm\nISBN 3-205-03175-X\n(Tabulae musicae Austriacae ; 6)\nIDN 730157679\n\nMichelitsch, Helga: Das Klavierwerk von Georg Christoph Wagenseil : thematischer Katalog / Helga Michelitsch. - Wien [u.a.] : Bählau, 1966. - 163 S.\n(Tabulae musicae Austriacae ; 3)\nIDN 457596414",
				"Wagenseil Orchester", "118770616", "XA-AT" },
			{// alt
				"Wagenseil, Georg Christoph",
				"S",
				"Das Klavierwerk von Georg Christoph Wagenseil : themat. Katalog / Helga Michelitsch. - Wien [u.a.] : Böhlau, 1966. - 163 S. (Tabulae musicae Austriacae ; 3)",
				"Wagenseil Klavier", "118770616", "XA-AT" },
			{
				"Wagner, Richard",
				"WWV",
				"Deathridge, John: Wagner-Werk-Verzeichnis : (WWV) ; Verzeichnis der musikalischen Werke Richard Wagners und ihrer Quellen ; erarb. im Rahmen d. Richard-Wagner-Gesamtausg. / John Deathridge ; Martin Geck ; Egon Voss. Red. Mitarb. Isolde Vetter. - Mainz [u.a.] : Schott, 1985. - 607 S., Noten. - 27 cm\nISBN 3-7957-2201-2\nIDN 871490455",
				"WagnerWV", "118594117", "XA-DE;XA-CH;XA-IT;XA-AT" },
			{
				"Weber, Carl Maria von",
				"J",
				"Jähns, Friedrich Wilhelm: Carl Maria von Weber in seinen Werken : chronologisch-thematisches Verzeichnis seiner sämmtlichen Compositionen nebst Angabe d. unvollst., verloren gegangenen, zweifelhaften u. untergeschobenen mit ... Berlin 1871 / Friedr. Wilh. Jähns. - Unveränd. Neuaufl. - Berlin : Lienau, 1967. - 476 S., 4 Bl. Faks.\nIDN 457087277",
				"WeberWV", "118629662", "XA-DE" },
			{
				"Wolf, Hugo",
				"HWW",
				"Jestremski, Margret: Hugo-Wolf-Werkverzeichnis : (HWW) ; thematisch-chronologisches Verzeichnis der musikalischen Werke Hugo Wolfs. - Kassel [u.a.] : Bärenreiter, 2011",
				"HWW", "118634712", "XA-AT" },
			{
				"Zachow, Friedrich Wilhelm",
				"LV",
				"Zachow, Friedrich Wilhelm: Gesammelte Werke für Tasteninstrumente: 1. Choralbearbeitungen ; 2. Freie Kompositionen / Friedrich Wilhelm Zachow. Hrsg. von Heinz Lohmann. - Wiesbaden : Breitkopf & Härtel, c 1966. - 30 cm Werkverz. S. III - IV\nIDN 979413885",
				"ZachowWV", "118772147", "XA-DE" },
			{// alt
				"Zachow, Friedrich Wilhelm",
				"L",
				"Zachow, Friedrich Wilhelm: Gesammelte Werke für Tasteninstrumente: 1. Choralbearbeitungen ; 2. Freie Kompositionen / Friedrich Wilhelm Zachow. Hrsg. von Heinz Lohmann. - Wiesbaden : Breitkopf & Härtel, c 1966. - 30 cm Werkverz. S. III - IV\nIDN 979413885",
				"ZachowWV", "118772147", "XA-DE" },
			{
				"Zelenka, Jan Dismas",
				"ZWV",
				"Reich, Wolfgang: Jan Dismas Zelenka : thematisch-systematisches Verzeichnis der musikalischen Werke ; (ZWV) / zsgest. von Wolfgang Reich. - Dresden : Sächsische Landesbibliothek. - 21 cm\nIDN 210488247\nNotenteil. - 1985",
				"ZelenkaWV", "118808370", "XA-CZ" },
			{
				"Zimmermann, Bernd Alois",
				"BAZ",
				"Henrich, Heribert: Bernd Alois Zimmermann Werkverzeichnis : Verzeichnis der musikalischen Werke von Bernd Alois Zimmermann und ihre Quellen / Heribert Henrich. Erstellt unter Verwendung der Vorarbeiten von Klaus Ebbeke. - Berlin : Akademie der Künste ; Mainz : Schott, 2013.",
				"BAZ", "11863691X", "XA-DE" }

		};

	/**
	 * Jeder Komponist hat ev. mehrere Werkverzeichnisse mit deren
	 * Abkürzungen.
	 */
	private static HashMap<String, LinkedList<String>> komp2ThIdxsMap =
		new HashMap<String, LinkedList<String>>();

	/**
	 * Alle Werkverzeichnisnummern.
	 */
	private static TreeSet<String> knownThIdx = new TreeSet<String>();

	/**
	 * Liefert alle Werkverzeichnisnummern.
	 * @return Alle Werkverzeichnisnummern.
	 */
	public static Set<String> getThematicIndices() {
		return new TreeSet<String>(knownThIdx);
	}

	static {
		Pattern patSlash = Pattern.compile("/");
		for (int i = 0; i < DATA.length; i++) {
			String komponist = DATA[i][0];
			LinkedList<String> thIOfComposer = komp2ThIdxsMap.get(komponist);
			if (thIOfComposer == null) {
				thIOfComposer = new LinkedList<String>();
				komp2ThIdxsMap.put(komponist, thIOfComposer);
			}
			String thI = DATA[i][THEMATIC_INDEX];
			String[] thIArr = patSlash.split(thI);
			List<String> thIList = Arrays.asList(thIArr);
			thIOfComposer.addAll(thIList);
			knownThIdx.addAll(thIList);
		}

	}

	public static LinkedList<String> getThematicIndices(final String composer) {
		return komp2ThIdxsMap.get(composer);
	}

	public static boolean isKnownThIdx(final String idx) {
		for (String s : knownThIdx) {
			if (s.equals(idx))
				return true;
		}
		return false;
	}

	public static ThematicIndexNumber matchThIdx(
			final String composer,
			final String addInfoStr) {
		if (addInfoStr == null)
			throw new IllegalArgumentException(
					"Null-String an matchThIdx()übergeben");
		if (addInfoStr.length() == 0)
			return null;

		//String s1 = zusatzString.trim();
		LinkedList<String> wvsKom = getThematicIndices(composer);

		String wv = knownThIdx.floor(addInfoStr);
		if (wv != null && StringUtils.matchesPrecisely(wv, addInfoStr)) {
			if (wvsKom != null && !wvsKom.contains(wv))
				throw new IllegalArgumentException("Komponist falsch");
			int pos = wv.length();
			ThematicIndexNumber thIdxNumber = new ThematicIndexNumber();
			thIdxNumber.abbreviation = wv;
			/* .trim() für den unwahrscheinlichen Fall, dass die wv-Zählung
			 * leer sein sollte:	 */
			thIdxNumber.number = addInfoStr.substring(pos).trim();
			// ohne WV-Nummer ziemlich absurd:
			if (thIdxNumber.number.length() == 0)
				return null;
			return thIdxNumber;
		}
		return null;
	}

	public static String getCountryCode(final String idn) {
		for (String[] line : DATA) {
			if (line[IDN].equals(idn))
				return line[COUNTRY_CODE];
		}
		return null;
	}

	public static String getSourceAbb(final String idn) {
		for (String[] line : DATA) {
			if (line[IDN].equals(idn))
				return line[SOURCE_ABB];
		}
		return null;
	}

	public static String getComposerID(final String compName) {
		for (String[] line : DATA) {
			if (line[NAME].equals(compName))
				return line[IDN];
		}
		return null;
	}

	public static boolean isValidThIdx(final String komponist, final String wv) {
		//	Hier muss man wohl von einem nicht
		// benannten Komponisten ausgehen
		if (komponist == null || komponist.equals(""))
			return true;

		LinkedList<String> lls = getThematicIndices(komponist);
		// Hier muss man wohl von einem unbekannten Komponisten ausgehen
		if (komponist.equals("") || lls == null)
			return true;

		// Der Komponist ist bekannt. Stimmt das WV mit einem Eintrag der
		// Liste überein?
		for (String string : lls) {
			if (string.equalsIgnoreCase(wv))
				return true;
		}
		return false;
	}

	//@formatter:on

	/**
	 * 
	 * @return	Liefert alle Komponisten, jeden nur einmal (an der idn
	 * 			erkannt).
	 */
	public static Set<Composer> getAllComposers() {
		Set<Composer> compSet = new TreeSet<Composer>();
		for (String[] compStr : DATA) {
			compSet.add(new Composer(compStr[NAME], compStr[THEMATIC_INDEX],
					compStr[SOURCE], compStr[SOURCE_ABB], compStr[IDN],
					compStr[COUNTRY_CODE]));
		}
		return compSet;
	}

	/**
	 * 
	 * @return	Alle Werkverzeichnisse mit Komponisten.
	 */
	public static List<Composer> getAllComposersAndSources() {
		List<Composer> compList = new LinkedList<Composer>();
		for (String[] compStr : DATA) {
			compList.add(new Composer(compStr[NAME], compStr[THEMATIC_INDEX],
					compStr[SOURCE], compStr[SOURCE_ABB], compStr[IDN],
					compStr[COUNTRY_CODE]));
		}
		return compList;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out.println(getThematicIndices());

	}

}
