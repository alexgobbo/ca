/*- Package Declaration ------------------------------------------------------*/

package org.epics.ca.examples;

/*- Imported packages --------------------------------------------------------*/

import net.jcip.annotations.ThreadSafe;
import org.epics.ca.impl.JavaProcessManager;
import org.epics.ca.util.logging.LibraryLogManager;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/*- Interface Declaration ----------------------------------------------------*/
/*- Class Declaration --------------------------------------------------------*/

@ThreadSafe
public class ExampleTest
{

/*- Public attributes --------------------------------------------------------*/
/*- Private attributes -------------------------------------------------------*/

   private static final Logger logger = LibraryLogManager.getLogger( ExampleTest.class );

/*- Main ---------------------------------------------------------------------*/
/*- Constructor --------------------------------------------------------------*/
/*- Public methods -----------------------------------------------------------*/
/*- Package-level methods ----------------------------------------------------*/

   @Test
   void runExample() throws InterruptedException
   {
      Properties properties = new Properties();
      JavaProcessManager exampleRunner = new JavaProcessManager( Example.class, properties, new String[] {} );
      final boolean startedOk = exampleRunner.start( true );
      assertThat( startedOk, is( true ) );
      final boolean completedOK = exampleRunner.waitFor(10, TimeUnit.SECONDS );
      assertThat( completedOK, is( true ) );
      assertThat( exampleRunner.getExitValue(), is( 0 ) );
   }

/*- Private methods ----------------------------------------------------------*/
/*- Nested Classes -----------------------------------------------------------*/

}
