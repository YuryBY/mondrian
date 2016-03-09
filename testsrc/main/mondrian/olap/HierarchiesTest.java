package mondrian.olap;

import mondrian.test.FoodMartTestCase;
import mondrian.test.TestContext;

import java.sql.SQLException;

/**
 * Created by Yury_Bakhmutski on 3/4/2016.
 */
public class HierarchiesTest extends FoodMartTestCase {

  public void testTimeDimFirstHierarchySsas() throws SQLException {
    propSaver.set(
        MondrianProperties.instance().SsasCompatibleNaming, true);
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Time].Name\n"
        + "    MEMBER Measures.LevelName as [Time].[Time].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Time].[Time].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Time\n" + "Row #0: Time\n" + "Row #0: Time\n";
    assertQueryReturns( mdx, expected );
    getTestContext().flushSchemaCache();
  }

  public void testTimeDimSecondHierarchySsas() throws SQLException {
    propSaver.set(
        MondrianProperties.instance().SsasCompatibleNaming, true);
    TestContext context = getTestContext().withFreshConnection();
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Time].Name\n"
        + "    MEMBER Measures.LevelName as [Time].[Weekly].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Time].[Weekly].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Time\n" + "Row #0: Weekly\n" + "Row #0: Weekly\n";
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

  public void testDateDimFirstHierarchySsas() throws SQLException {
    propSaver.set(
        MondrianProperties.instance().SsasCompatibleNaming, true);
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Date].Name\n"
        + "    MEMBER Measures.LevelName as [Date].[Date].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Date].[Date].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";

    String dateDim = "<Dimension name=\"Date\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n"
        + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Date\n" + "Row #0: Date\n" + "Row #0: Date\n";
    TestContext context = getTestContext().withFreshConnection();
    context = context.createSubstitutingCube( "Sales", dateDim );
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

  public void testDateDimSecondHierarchySsas() throws SQLException {
    propSaver.set(
        MondrianProperties.instance().SsasCompatibleNaming, true);
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Date].Name\n"
        + "    MEMBER Measures.LevelName as [Date].[Weekly].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Date].[Weekly].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";

    String dateDim = "<Dimension name=\"Date\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n"
        + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Date\n" + "Row #0: Weekly\n" + "Row #0: Weekly\n";
    TestContext context = getTestContext().withFreshConnection();
    context = context.createSubstitutingCube( "Sales", dateDim );
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

  public void testTimeDimNullHierarchy() throws SQLException {
    propSaver.set(
        MondrianProperties.instance().IgnoreInvalidMembers, true);
    String mdx = "With member [Measures].[DimensionName] as [Time].Name\n"
        + "  Member [Measures].[LevelName] as [Time].[#null].Hierarchy.Name\n"
        + "  Member [Measures].[MemberName] as [Time].[#null].Hierarchy.Name\n"
        + "Select {[Measures].[DimensionName], [Measures].[LevelName], [Measures].[MemberName]} ON COLUMNS\n"
        + "From [Sales]";
    executeQuery( mdx );
    getTestContext().flushSchemaCache();
  }

  public void testTimeDimFirstHierarchy() throws SQLException {
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Time].Name\n"
        + "    MEMBER Measures.LevelName as [Time].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Time].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Time\n" + "Row #0: Time\n" + "Row #0: Time\n";
    assertQueryReturns( mdx, expected );
    getTestContext().flushSchemaCache();
  }

  public void testTimeDimSecondHierarchy() throws SQLException {
    TestContext context = getTestContext().withFreshConnection();
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Time].Name\n"
        + "    MEMBER Measures.LevelName as [Time.Weekly].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Time.Weekly].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Time\n" + "Row #0: Time.Weekly\n" + "Row #0: Time.Weekly\n";
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

  public void testDateDimFirstHierarchy() throws SQLException {
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Date].Name\n"
        + "    MEMBER Measures.LevelName as [Date].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Date].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";

    String dateDim = "<Dimension name=\"Date\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n"
        + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Date\n" + "Row #0: Date\n" + "Row #0: Date\n";
    TestContext context = getTestContext().withFreshConnection();
    context = context.createSubstitutingCube( "Sales", dateDim );
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

  public void testDateDimSecondHierarchy() throws SQLException {
    String mdx = "WITH\n" + "\n" + "    MEMBER Measures.DimensionName AS [Date].Name\n"
        + "    MEMBER Measures.LevelName as [Date.Weekly].[Year].Hierarchy.Name\n"
        + "    MEMBER Measures.MemberName as [Date.Weekly].[1997].Hierarchy.Name\n" + "\n"
        + "    SELECT {Measures.DimensionName, Measures.LevelName, Measures.MemberName} ON 0\n" + "    from [Sales]";

    String dateDim = "<Dimension name=\"Date\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n"
        + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeWeeks\"/>\n"
        + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeDays\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>";
    String expected =
        "Axis #0:\n" + "{}\n" + "Axis #1:\n" + "{[Measures].[DimensionName]}\n" + "{[Measures].[LevelName]}\n"
            + "{[Measures].[MemberName]}\n" + "Row #0: Date\n" + "Row #0: Date.Weekly\n" + "Row #0: Date.Weekly\n";
    TestContext context = getTestContext().withFreshConnection();
    context = context.createSubstitutingCube( "Sales", dateDim );
    context.assertQueryReturns( mdx, expected );
    context.flushSchemaCache();
  }

}

