# Compare Utils

[ ![Download](https://maven-badges.herokuapp.com/maven-central/io.github.nejckorasa/compare-utils-core/badge.svg) ](https://maven-badges.herokuapp.com/maven-central/io.github.nejckorasa/compare-utils-core)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/48793689ebd14073b3875b427792404d)](https://www.codacy.com/app/nejckorasa/compare-utils?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nejckorasa/compare-utils&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.com/nejckorasa/compare-utils.svg?branch=master)](https://travis-ci.com/nejckorasa/compare-utils)
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)]( https://twitter.com/intent/tweet?url=https%3A%2F%2Fgithub.com%2Fnejckorasa%2Fcompare-utils&via=nejckorasa&text=Great%20Java%20Compare%20Diff%20Utils%20library%20to%20compare%20objects%20and%20collecitons&hashtags=java%2Cgithub%2Ccode%2Cdevelopment%2Cdevelopers%2Cprogramming%2Cprogrammers%2Ccomparator%2Csoftware%2Cdeveloping)

## Intro 

Comparing of Java Collections and Objects made easy.

Minimalistic library written in core Java with no other dependencies. 

It provides easy way to compare **Collections** and **Objects** of same or different class when Java's equals functions and Java's Comparators don't suffice. 

Collections compare result is presented with clear separation of **added**, **removed**, **updated** and **unchanged** items.

See [Javadoc documentation](https://nejckorasa.github.io/compare-utils/).

## Add to your project 

#### Maven

```xml
<dependency>
  <groupId>io.github.nejckorasa</groupId>
  <artifactId>compare-utils-core</artifactId>
  <version>1.0.2-RELEASE</version>
</dependency>
```

#### Gradle

```gradle
compile 'io.github.nejckorasa:compare-utils:1.0.2-RELEASE'
```

## Usage

At first glance it might seem that this is no different from using Java's own `Comparator` and even Java's `equals` functions. 
For some use cases it is indeed easier to use `Comparator`, and you should do so! 

A few examples where this library is useful:

- **Compare collections of different object classes**
    - Specify `keyExtractor` and your own `equals` function
    - Instead of writing equals function, you can compare objects by comparing only some of its fields

- **Compare objects (of same or different class) by comparing only some of its fields**
    - Define field extractors

#### Example

Given some classes and 2 collections:

```java
static SomeClass {
    long id;
    int firstProperty;
    String secondProperty;
    // other fields emitted...
}

static OtherClass {
    long id;
    int propertyOne;
    String propertyTwo;
    // other fields emitted...
}
```
Compare collections of different classes by only comparing 2 of their fields and matching by `id`:

```java
List<SomeClass> firstList;
List<OtherClass> secondList;

CollectionCmp
    .of(firstList, secondList, o1 -> o1.getId(), o2 -> o2.getId())
    .compare(
        EqualityPair.of(o1 -> o1.firstProperty(), o2 -> o2.propertyOne()),
        EqualityPair.of(o1 -> o1.secondProperty(), o2 -> o2.propertyTwo())
);
```

Compare objects of different classes by only comparing 2 of their fields:

```java
SomeClass first;
OtherClass second;

ObjectCmp.equals(
        first,
        second,
        EqPair.of((o1 -> o1.firstProperty(), o2 -> o2.propertyOne()),
        EqPair.of(o1 -> o1.secondProperty(), o2 -> o2.propertyTwo())
);
```

Find more examples in [tests](src/test/java/io/github/nejckorasa). 

## Collections compare

#### Basics

It provides comparing and finding differences between two collections (_base_ and _working_). Items in collections can be of same or different classes. Two steps are important to understand how comparison is made and how differences are found:

1. **Matching**

   First, items from both collections are matched together by their keys. Provided `keyExtractor` functions are used to extract the keys. When 2 items match they form a **Pair**.

2. **Comparing**

   Provided `equalsFunction`, `equalities` or `equalityPairs` are used to compare the pairs.

#### Matching

Matching is computed using keyExtractor functions, for example `i -> i.getId()`:

```java
CollectionCmp
        .of(baseList, workingList, i -> i.getId())
        .compare();
```
or when comparing collections of different item classes:

```java
CollectionCmp
        .of(baseList, workingList, b -> b.getId(), w -> w.getId()) // keyExtractor functions for base and working items
        .compare(); // keyExtractor functions for base and working items
```

> **keyExtractors** are not optional and must always be provided.

#### Comparing

Comparing is performed on items that are matched together (they form a Pair). This is done by equals function which can be defined in a few different ways, using `compare(...)` function:

_equalsFunction_

```java
CollectionCmp
        .of(baseList, workingList, i -> i.getId())
        .compare((i1, i2) -> i1.getName().equals(i2.getName()));
```

_equalities_ or _equality pairs_

```java
CollectionCmp
        .of(baseList, workingList, i -> i.getId())
        .compare(
          item -> item.getName(), 
          item -> item.getCode(), 
          item -> item.getDescription());
```

In example above, items are considered equal when `name`, `code` and `description` fields are equal. Similarly with collections of different classes, equality pairs are used:

```java
CollectionCmp
        .of(baseList, workingList, b -> b.getId(), w -> w.getId())
        .compare(
            EqPair.of(b -> b.getName(), w -> w.getData().getName()),
            EqPair.of(b -> b.getCode(), w -> w.getData().getCode()));
```
Now, items are considered equal when `name` and `code` fields are equal. Because base and working items are not of same class, fields may exist on different paths.

> **equalsFunction** is optional, by default `Objects.equals()` is used to compare matched items.

#### Result

Compare result of collections is presented with clear separation of **added**, **removed**, **updated** and **unchanged** items. Result object has a few useful functions to help you analyze result data:

```java
var compareResult = CollectionCmp
        .of(baseList, workingList, i -> i.getId())
        .compare(i -> i.getName());

boolean hasChanges = compareResult.hasChanges();
int changesCount = compareResult.getChangesCount();

boolean hasDifferences = compareResult.hasDifferences();
int differentCount = compareResult.getDifferentCount();

compareResult.getAll();
compareResult.getAdded();
compareResult.getUdpated();

// changed are all items that were added, removed or updated
compareResult.getChanged();
compareResult.getUncanged();

// stream through changed, unchanged, added, different items ...
compareResult.streamChanged()
```

All result data is provided in Pairs, containing matched base and working item as well as difference type:

```java
CmpPair<B, W> pair = ...

B base = pair.getBase();
W working = pair.getWorking();

Diff diff = pair.getDiff(); // UNCHANGED, UPDATED, ADDED, REMOVED
Serializable key = pair.getKey(); // key by which items are matched together
```

#### Partitioning 

Matching must be a **injective** function (in both ways) == there must be at most one item with the same key in each collection. If that is not true, collection cannot be partitioned and collections compare result might be incorrect.

You can check if collection can be partitioned using:

```java
boolean canPartition = CollectionCmpPartitioner.canPartition(collection, keyExtractor)
```

## Objects compare

Objects are compared using same features as comparing collections above, for example:

```java
// check if objects are equal based on it's name, code and description
boolean equals = ObjectCmp.equals(
    object1, 
    object2, 
    o -> o.getName(), o -> o.getCode(), o -> o.getDescription());
```

## Contributing

Pull requests are welcome, [Show your ❤ with a ★](https://github.com/nejckorasa/compare-utils/stargazers)
