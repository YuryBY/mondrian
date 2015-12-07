/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2002-2005 Julian Hyde
// Copyright (C) 2005-2015 Pentaho and others
// All Rights Reserved.
*/
package mondrian.olap;

/**
 * The exception indicates that the number of members in the SQL query
 * "in" clause exceeds the value derived by {@link MondrianProperties#MaxConstraints}
 * property
 *
 * @author Yury_Bakhmutski
 */
public class NativeEvaluationInClauseLimitException
    extends ResultLimitExceededException
{

  /**
  * NativeEvaluationInClauseLimitException.
  *
  * @param message Localized error message
  */
  public NativeEvaluationInClauseLimitException(String message) {
    super(message);
  }
}

// End NativeEvaluationInClauseLimitException.java
