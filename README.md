# banner-plus-plus
Library mod for the Fabric platform that allows mods to add banner patterns in a hastle-free way. 

## Dependents
To depend on Banner++, add the following to your `build.gradle` repository block.

```groovy
repositories {
	maven {
		url 'http://server.bbkr.space:8081/artifactory/libs-release'
	}
}
```

## Adding Custom Patterns
Custom patterns are encapsulated in `LoomPattern` objects, which are registered in a vanilla Minecraft registry. Thus, you can register custom patterns by simply adding a new `LoomPattern` to the registry.

```java
// add a normal pattern
Registry.register(LoomPatterns.REGISTRY, new Identifier("modid", "my_pattern"), new LoomPattern(false));
// add a special pattern
Registry.register(LoomPatterns.REGISTRY, new Identifier("modid", "my_pattern"), new LoomPattern(true));
```
Normal patterns are those that don't need an item to be placed in the loom in order to select them, while special patterns do need such an item.

Items associated with special patterns are instances of `LoomPatternItem`, the analogue of the vanilla `BannerPatternItem`. Simply register one of these items with each special pattern, and they will automatically be usable in the loom.

```java
Registry.register(Registry.ITEM, new Identifier("modid", "my_pattern_item"), new LoomPatternItem(MY_PATTERN, itemSettings));
```

## Resouce Keys

Loom pattern textures are stored under `modid:textures/pattern/banner/my_pattern.png` and `modid:textures/pattern/shield/my_pattern.png`. Language keys have the form `bannerpp.pattern.modid.my_pattern`.
