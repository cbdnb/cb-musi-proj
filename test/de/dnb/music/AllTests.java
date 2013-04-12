package de.dnb.music;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import utils.StringUtilsTest;
import utils.TitleUtilsTest;

import de.dnb.music.additionalInformation.ThematicIndexDBTest;
import de.dnb.music.additionalInformation.TonartTest;
import de.dnb.music.additionalInformation.ZusatzangabeTest;
import de.dnb.music.genre.GenreDBTest;
import de.dnb.music.genre.GenreListTest;
import de.dnb.music.genre.ParseGenreTest;
import de.dnb.music.mediumOfPerformance.InstrumentDBTest;
import de.dnb.music.mediumOfPerformance.ParseInstrumentationTest;
import de.dnb.music.publicInterface.TransformRecordTest;
import de.dnb.music.title.ParseMusicTitleTest;
import de.dnb.music.title.PartOfWorkTest;
import de.dnb.music.version.ParseVersionTest;
import de.dnb.music.version.VersionTest;
import de.dnb.music.visitor.AdditionalDataIn3XXVisitorTest;
import de.dnb.music.visitor.AuthorityDataIn3XXVisitorTest;

@RunWith(Suite.class)
@SuiteClasses({ InstrumentDBTest.class, ParseInstrumentationTest.class,
	GenreDBTest.class, ParseGenreTest.class, TonartTest.class,
	ZusatzangabeTest.class, ParseGenreTest.class, ParseMusicTitleTest.class,
	ParseVersionTest.class, VersionTest.class, StringUtilsTest.class,
	GenreListTest.class, PartOfWorkTest.class,
	AuthorityDataIn3XXVisitorTest.class, AdditionalDataIn3XXVisitorTest.class,
	ThematicIndexDBTest.class, TransformRecordTest.class, TitleUtilsTest.class })
public class AllTests {
	// Body nicht nötig
}
