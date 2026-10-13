#version 410 core

#extension GL_ARB_gpu_shader_fp64 : enable

out vec4 FragColor;
in vec3 outColor;

uniform int width, height;
uniform vec2 scale;
uniform vec2 offsetX, offsetY;

float aspectRatio = float( width ) / float( height );

int maxIter = 2000;
float colorScale = maxIter / 255.0;

vec2 sum( float a, float b ) {
    if ( abs( a ) < abs( b ) ) {
        float temp = a;
        a = b;
        b = temp;
    }
    float s = a + b;
    float z = s - a;
    precise float t = b - z;
    return vec2( s, t );
}

vec2 sum( vec2 a, vec2 b ) {
    vec2 highSum = sum( a.x, b.x );
    vec2 lowSum = sum( a.y, b.y );

    vec2 correctionSum = sum( highSum.y, lowSum.x );
    vec2 result = sum( highSum.x, correctionSum.x );
    float correction = lowSum.y + correctionSum.y + result.y;

    return sum( result.x, correction );
}

vec2 sub( vec2 a, vec2 b ) {
    vec2 highSum = sum( a.x, -b.x );
    vec2 lowSum = sum( a.y, -b.y );

    vec2 correctionSum = sum( highSum.y, lowSum.x );
    vec2 result = sum( highSum.x, correctionSum.x );
    float correction = lowSum.y + correctionSum.y + result.y;

    return sum( result.x, correction );
}

vec2 mul( vec2 a, vec2 b ) {
    float high = a.x * b.x;
    precise float low = fma( a.x, b.x, -high );
    low = fma( a.x, b.y, low );
    low = fma( a.y, b.x, low );
    low = fma( a.y, b.y, low );

    return sum( high, low );
}

float screenX = ( 2.0 * gl_FragCoord.x / float( width ) - 1.0 ) * aspectRatio;
float screenY = ( 2.0 * gl_FragCoord.y / float( height ) - 1.0 );

vec2 x = sum( offsetX, mul( scale, vec2( screenX, 0.0 ) ) );
vec2 y = sum( offsetY, mul( scale, vec2( screenY, 0.0 ) ) );

float remap( float x, float x1, float x2, float y1, float y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

void main() {
    vec2 zx = vec2( 0.0f );
    vec2 zy = vec2( 0.0f );
    vec2 x2, y2;

    int i = 0;
    while ( i < maxIter ) {
        x2 = mul( zx, zx );
        y2 = mul( zy, zy );

        if ( x2.x + y2.x > 4.0 ) break;

        vec2 yNew = sum( mul( mul( vec2( 2.0f, 0.0f ), zx ), zy ), y );
        zx = sum( mul( -zy, zy ), sum( x2, x ) );
        zy = yNew;
        i++;
    }
    float color = 0;
    if ( i < maxIter ) color = remap( i / colorScale, 0, 255, 0, 1 );

    FragColor = vec4( color * 2, color / 2, color, 1.0f );
}

