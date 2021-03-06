package com.kurukurupapa.pffsimu.domain.partyfinder.impl2;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.kurukurupapa.pffsimu.test.SlowTests;

@RunWith(Categories.class)
@SuiteClasses({ MemoriaItemCombinationsTest.class,
		NextMagicAccessoryTest.class, NextMemoriaTest.class,
		NextWeaponTest.class, PartyFinder2aTest.class, PartyFinder2bTest.class,
		PartyFinder2cTest.class, PartyFinder2dTest.class,
		PartyFinder2dUserDataTest.class })
@ExcludeCategory(SlowTests.class)
public class AllFastTests {

}
