% card io testing
%
% Milli [23th September 2014]

clear all;
close all;

% read image
I = imread('1-0.png');
Ig = rgb2gray(I);

% size for ref
s = size(Ig);
printf('dimensions of the image %d X %d\n', s(1), s(2));

% Construct inp pixel set
data = zeros(s(1)*s(2), 3);

rowno = 1;
for(i = 1:s(1))
	for(j = 1:s(2))
		pix = Ig(i, j);
				
		% set the point in data
		data(rowno, :) = [i, j, pix];
		rowno++;		
	endfor
endfor

% run db scan here


