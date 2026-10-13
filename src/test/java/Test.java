import org.joml.Vector2f;

public class Test {
    void main() {
        double a = 1.0000000000012;
        double b = 1.0000000039;
        Vector2f dsA = toDoubleSingle( a );
        Vector2f dsB = toDoubleSingle( b );

        double f64 = a - b;
        float f32 = ( float ) a - ( float ) b;
        Vector2f doubleSingle = sub( dsA, dsB );

        IO.println( a + " - " + b );
        IO.println( "f64: " + f64 );
        IO.println( "f32: " + ( f32 - f64 ) );
        IO.println( "ds: " + ( ( double ) doubleSingle.x + doubleSingle.y - f64 ) );
        IO.println( doubleSingle.x + " " + doubleSingle.y );
    }

    Vector2f toDoubleSingle( double value ) {
        float high = ( float ) value;
        float low = ( float ) ( value - ( double ) high );
        return new Vector2f( high, low );
    }

    Vector2f prod( Vector2f a, Vector2f b ) {
        float highA = a.x;
        float highB = b.x;
        float lowA = a.y;
        float lowB = b.y;

        float high = highA * highB;
        double low = ( double ) highA * highB - high;
        low += ( double ) highA * lowB;
        low += ( double ) highB * lowA;

        return sum( high, ( float ) low );
    }

    Vector2f prod( float a, float b ) {
        float high = a * b;
        float low = ( float ) ( ( double ) a * b - high );
        return new Vector2f( high, low );
    }

    Vector2f sub( Vector2f a, Vector2f b ) {
        float highA = a.x;
        float highB = b.x;
        float lowA = a.y;
        float lowB = b.y;

        Vector2f highSum = sum( highA, -highB );
        Vector2f lowSum = sum( lowA, -lowB );

        Vector2f correctionSum = sum( highSum.y, lowSum.x );
        Vector2f result = sum( highSum.x, correctionSum.x );
        float correction = lowSum.y + correctionSum.y + result.y;

        return sum( result.x, correction );
    }

    Vector2f sum( Vector2f a, Vector2f b ) {
        float highA = a.x;
        float highB = b.x;
        float lowA = a.y;
        float lowB = b.y;

        Vector2f highSum = sum( highA, highB );
        Vector2f lowSum = sum( lowA, lowB );

        Vector2f correctionSum = sum( highSum.y, lowSum.x );
        Vector2f result = sum( highSum.x, correctionSum.x );
        float correction = lowSum.y + correctionSum.y + result.y;

        return new Vector2f( result.x, correction );
    }

    Vector2f sum( float a, float b ) {
        if ( Math.abs( a ) < Math.abs( b ) ) {
            float temp = a;
            a = b;
            b = temp;
        }
        float s = a + b;
        float z = s - a;
        float t = b - z;
        return new Vector2f( s, t );
    }
}
